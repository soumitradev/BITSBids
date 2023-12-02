import Navbar from "~/components/Navbar.tsx";
import {A} from "@solidjs/router";
import {Input} from "~/components/ui/input.tsx";
import {Button} from "~/components/ui/button.tsx";

function UserProfile() {
    // const navigate = useNavigate();
    // const [room, setRoom] = createSignal("");
    // const [phone, setPhone] = createSignal(0);
    //
    // onMount(async () => {
    //     const loggedIn = await fetch("/api/user/me");
    //     if (loggedIn.status === 200) {
    //         const data = loggedIn.json();
    //         setRoom(data.room)
    //     } else if (loggedIn.status === 500) {
    //         showToast({
    //             title: "Server Error",
    //             description: "The server might be down",
    //             variant: "destructive",
    //         });
    //     }
    // });
    // const edit = async () => {
    //     const editData = {
    //         room: room(),
    //         phoneNumber: phone(),
    //     };
    //     const edited = await fetch("/api/user/edit", {
    //         method: "POST",
    //         body: JSON.stringify(editData),
    //         headers: {
    //             Accept: "application/json",
    //             "Content-Type": "application/json",
    //         },
    //     });
    //     const data = await edited.json();
    //     if (edited.status === 200) {
    //         navigate("/UserProfile");
    //     } else if (registered.status === 401) {
    //         showToast({
    //             title: "Unauthorized",
    //             description: data.data.error + ": " + data.data.cause,
    //         });
    //     } else if (registered.status === 400) {
    //         showToast({
    //             title: "Bad Request",
    //             description: data.data.error + ": " + data.data.cause,
    //         });
    //     } else {
    //         showToast({
    //             title: "Server Error",
    //             description: "The server might be down",
    //             variant: "destructive",
    //         });
    //     }
    // };


    return (
        <>
            <Navbar/>
            <div class=" min-h-screen justify-around items-centre">
                <div class="flex  insert-20 justify-around ">
                    <div class="flex flex-col mx-10 justify-around items-center">
                        <div></div>
                        <div>
                        <span class="xl:text-3xl text-2xl ">
                        Personal Profile
                        </span>
                        </div>
                        <div><Input
                            id="Name"
                            class="mb-4"
                            placeholder="name"

                            readonly
                        />
                        </div>
                        <div><Input
                            id="Batch"
                            class="mb-4"
                            placeholder="Batch"
                        /></div>
                        <div><Input
                            id="phone"
                            class="mb-4"
                            placeholder="Mobile Number"
                            maxlength="10"
                            // onChange={(e) => setPhone(e.target.value)}
                        /></div>
                        <div><Input
                            id="room"
                            class="mb-4"
                            placeholder="Room Number"
                            // onChange={(e) => setRoom(e.target.value)}
                        /></div>
                        <div class="align-left">
                            <Button>Save Changes</Button>
                        </div>
                    </div>
                    <div class="flex flex-col mx-10">
                        <div class="text-3xl">Current Balance</div>
                        <div></div>
                        <div class="flex ">
                            <div></div>
                            <div></div>
                        </div>
                        <A href="/Home">View Listed Items</A>
                        <A href={"/YourBids"}>View my bids</A>


                    </div>


                </div>
            </div>

        </>
    );
}

export default UserProfile;
