import { onMount } from "solid-js";
import Mesh from "../components/Mesh";
import { showToast } from "~/components/ui/toast";
import { useNavigate } from "@solidjs/router";

function Login() {
  const navigate = useNavigate();

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

  return (
    <>
      <Mesh className="-z-10 fixed xl:scale-90 scale-75 -left-[400%] md:-left-[200%] lg:-left-full xl:-left-[60%] md:-top-1/3 lg:-top-1/4 -top-1/2 -rotate-[6deg]" />
      <div class="text-center mt-20 overflow-hidden">
        <div class="flex flex-col items-center">
          <span class="md:text-6xl text-5xl font-extrabold">BITSBids</span>
          <a
            class="font-bold text-md w-fit mt-64 bg-slate-50 text-slate-900 rounded-lg p-2 px-3 flex"
            href="/api/oauth2/authorization/google"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="lucide lucide-at-sign mr-2"
            >
              <circle cx="12" cy="12" r="4" />
              <path d="M16 8v5a3 3 0 0 0 6 0v-1a10 10 0 1 0-4 8" />
            </svg>
            Login with Google
          </a>
        </div>
      </div>
    </>
  );
}

export default Login;
