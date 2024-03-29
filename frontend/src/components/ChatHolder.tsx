import MessageBubble from "./MessageBubble";
import { Label } from "./ui/label";
import { Input } from "~/components/ui/input";
import { BiRegularArrowBack } from "solid-icons/bi";
import { AiOutlineInfoCircle } from "solid-icons/ai";
import { BsSend } from "solid-icons/bs";
import { Button } from "./ui/button";
import { useChat } from "~/context/chat";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "~/components/ui/popover";
import { For, createEffect, createSignal, on, onMount } from "solid-js";

const ChatHolder = (props: { notification: number }) => {
  const [state, { setChatId, setConversation }] = useChat();
  const [userDetails, setUserDetails] = createSignal(null);
  const conversation = state.conversations[state.chatId];
  const [messages, setMessages] = createSignal(conversation.messages);

  let inputRef: any;
  const sendMessage = async () => {
    const messageText = inputRef.value;
    inputRef.value = "";
    if (!messageText || !messageText.trim()) return;
    console.log(messageText);
    const messageData = {
      text: messageText,
    };
    const res = await fetch(`/api/product/${conversation.product.id}/send`, {
      method: "POST",
      body: JSON.stringify(messageData),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    const { data } = await res.json();
    //@ts-ignore
    setMessages([...messages(), { ...data, sentAt: new Date() }]);
  };

  onMount(() => {
    const element = document.getElementById(
      conversation.product.sellerId === state.userId
        ? `message-${conversation.lastReadBySellerId}`
        : `message-${conversation.lastReadByBuyerId}`
    );
    element?.scrollIntoView({ behavior: "instant", block: "end" });
  });

  onMount(async () => {
    const req = await fetch(
      `/api/user/${(conversation.buyerId != state.userId
        ? conversation.buyerId
        : conversation.sellerId
      ).toString()}`
    );
    console.log(req);
    if (req.status == 200) {
      const { data } = await req.json();
      console.log(data);
      setUserDetails(data);
    }
  });

  createEffect(() => {
    fetch(`/api/product/${conversation.product.id}/readMessages`, {
      method: "POST",
      body: JSON.stringify({ messageId: messages()[messages().length - 1].id }),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
  });

  createEffect(
    on(
      messages,
      () => {
        const element = document.getElementById(
          `message-${messages()[messages().length - 1].id}`
        );
        element?.scrollIntoView({ behavior: "smooth", block: "end" });
      },
      { defer: true }
    )
  );

  createEffect(
    on(
      () => props.notification,
      () => {
        if (props.notification != state.chatId) return;
        fetch(`/api/product/${conversation.product.id}/messages`).then((raw) =>
          raw.json().then(({ data }) => {
            setMessages(data);
            setConversation({ ...conversation, messages: data });
          })
        );
      }
    )
  );

  return (
    <div class="flex flex-col py-2 h-96">
      <div class="flex flex-row items-center w-full gap-2 border-b px-2 pb-1">
        <BiRegularArrowBack onClick={() => setChatId(-1)} />
        <Label class="text-lg font-semibold">{conversation.product.name}</Label>
        {userDetails() && (
          <Popover>
            <PopoverTrigger>
              <AiOutlineInfoCircle />
            </PopoverTrigger>
            <PopoverContent>
              <div class="flex flex-col gap-1">
                <p>
                  {/* @ts-ignore */}
                  <b>Name:</b> {userDetails()?.name}
                </p>
                <p>
                  {/* @ts-ignore */}
                  <b>Email:</b> {userDetails()?.email}
                </p>
                <p>
                  {/* @ts-ignore */}
                  <b>Phone Number:</b> {userDetails()?.phoneNumber}
                </p>
                <p>
                  {/* @ts-ignore */}
                  <b>Room:</b> {userDetails()?.room}
                </p>
              </div>
            </PopoverContent>
          </Popover>
        )}
      </div>
      <div class="p-2 flex flex-col overflow-auto gap-1.5">
        <For each={messages()}>
          {(m: any) => (
            <MessageBubble
              id={`message-${m.id}`}
              text={m.text}
              timestamp={m.sentAt}
              fromSelf={
                !m.fromBuyer ===
                (conversation.product.sellerId === state.userId)
              }
            ></MessageBubble>
          )}
        </For>
      </div>
      <div class="flex flex-row px-2 gap-1 mt-1">
        <Input
          placeholder="Message"
          class="focus-visible:ring-0"
          ref={inputRef}
        />
        <Button
          type="submit"
          class="px-3"
          variant="secondary"
          onClick={() => sendMessage()}
        >
          <BsSend size={20} />
        </Button>
      </div>
    </div>
  );
};

export default ChatHolder;
