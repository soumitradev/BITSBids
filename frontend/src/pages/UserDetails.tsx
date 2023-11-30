import { createSignal, onMount } from "solid-js";
import Mesh from "../components/Mesh";
import { showToast } from "~/components/ui/toast";
import { useNavigate } from "@solidjs/router";
import { Input } from "~/components/ui/input";
import { Label } from "~/components/ui/label";
import { Button } from "~/components/ui/button";

function UserDetails() {
  const navigate = useNavigate();
  const [room, setRoom] = createSignal("");
  const [phone, setPhone] = createSignal("");

  onMount(async () => {
    const loggedIn = await fetch("/api/user/me");
    if (loggedIn.status === 200) {
      navigate("/home");
    } else if (loggedIn.status === 500) {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }
  });

  const register = async () => {
    const registerData = {
      room: room(),
      phoneNumber: phone(),
    };
    const registered = await fetch("/api/user/create", {
      method: "POST",
      body: JSON.stringify(registerData),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    const data = await registered.json();
    if (registered.status === 201) {
      navigate("/home");
    } else if (registered.status === 401) {
      showToast({
        title: "Unauthorized",
        description: data.data.error + ": " + data.data.cause,
      });
    } else if (registered.status === 400) {
      showToast({
        title: "Bad Request",
        description: data.data.error + ": " + data.data.cause,
      });
    } else {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }
  };

  return (
    <>
      <div class="hidden lg:block">
        <Mesh className="-z-10 fixed xl:scale-90 scale-75 -left-[150%] xl:-left-[85%] -top-[45%] -rotate-[6deg]" />
      </div>
      <div class="border-l-2 w-full lg:w-1/2 fixed right-0 h-full bg-slate-950 px-16 py-12">
        <span class="xl:text-5xl text-4xl font-extrabold">
          Finish Registering
        </span>
        <div class="pt-12">
          <Label for="room" class="text-lg">
            Room
          </Label>
          <Input
            id="room"
            class="mb-4"
            placeholder="Room"
            onChange={(e) => setRoom(e.target.value)}
          />
          <Label for="phone" class="text-lg">
            Phone Number
          </Label>
          <Input
            id="phone"
            placeholder="Phone Number"
            onChange={(e) => setPhone(e.target.value)}
          />
          <Button class="mt-6 text-md font-bold" onClick={register}>
            Register
          </Button>
        </div>
      </div>
    </>
  );
}

export default UserDetails;
