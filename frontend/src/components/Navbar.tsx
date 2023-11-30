import { Avatar, AvatarFallback } from "~/components/ui/avatar";
import { A } from "@solidjs/router";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "~/components/ui/dropdown-menu";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "~/components/ui/popover";
import { Button } from "./ui/button";
import { createSignal, onMount } from "solid-js";
import { showToast } from "./ui/toast";
import { useNavigate } from "@solidjs/router";
import { Skeleton } from "./ui/skeleton";
import SearchBar from "./Search";
import ChatWindow from "./ChatWindow";
import { ChatProvider } from "~/context/chat";

const Navbar = () => {
  const navigate = useNavigate();
  const [userInitial, setUserInitial] = createSignal("");

  onMount(async () => {
    const loggedIn = await fetch("/api/user/me");
    if (loggedIn.status === 401 || loggedIn.status === 404) {
      navigate("/");
    } else if (loggedIn.status === 200) {
      const user = await loggedIn.json();
      setUserInitial(user.data.name[0]);
    } else {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }
  });

  return (
    <nav class="md:py-2 md:px-4 py-1 px-3 border-b-2 flex justify-between">
      <div class="flex items-center">
        <A class="md:text-2xl text-xl font-extrabold md:pr-6 pr-3" href="/home">
          BITSBids
        </A>
        <A
          class="md:text-lg text-md text-slate-400 duration-500 hover:text-slate-50 ease-in-out transition-all"
          href="/categories"
          activeClass="text-slate-50"
        >
          Categories
        </A>
        <Button class="font-bold md:ml-6 ml-3 py-2 h-fit px-3">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="3"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-plus md:mr-1"
          >
            <path d="M5 12h14" />
            <path d="M12 5v14" />
          </svg>
          <span class="hidden md:inline">List Product</span>
        </Button>
      </div>
      <div class="flex items-center">
        <SearchBar />
        <Popover>
          <PopoverTrigger class="md:mr-4 mr-2">
            <Button
              class="rounded-full md:w-10 md:h-10 h-8 w-8 p-0"
              variant={"secondary"}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-send md:w-5 md:h-5 h-4 w-4"
              >
                <path d="m22 2-7 20-4-9-9-4Z" />
                <path d="M22 2 11 13" />
              </svg>
            </Button>
          </PopoverTrigger>
          <PopoverContent class="mr-16 p-0 w-96">
            <ChatProvider>
              <ChatWindow />
            </ChatProvider>
          </PopoverContent>
        </Popover>
        <DropdownMenu>
          <DropdownMenuTrigger
            as={userInitial() ? Avatar : Skeleton}
            class={
              userInitial()
                ? "md:h-12 md:w-12 h-10 w-10"
                : "md:h-12 md:w-20 h-10 w-16 rounded-full p-0"
            }
          >
            {userInitial() ? (
              <AvatarFallback>{userInitial()}</AvatarFallback>
            ) : (
              <span></span>
            )}
          </DropdownMenuTrigger>
          {userInitial() ? (
            <DropdownMenuContent class="w-48">
              <DropdownMenuLabel>My Account</DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuItem>Account Settings</DropdownMenuItem>
              <DropdownMenuItem>My Bids</DropdownMenuItem>
              <DropdownMenuItem>My Products</DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem>Logout</DropdownMenuItem>
            </DropdownMenuContent>
          ) : (
            <span></span>
          )}
        </DropdownMenu>
      </div>
    </nav>
  );
};

export default Navbar;
