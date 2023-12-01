import MessageBubble from "./MessageBubble";
import { Label } from "./ui/label";
import { Input } from "~/components/ui/input";
import { BiRegularArrowBack } from "solid-icons/bi";
import { BsSend } from "solid-icons/bs";
import { Button } from "./ui/button";
import { useChat } from "~/context/chat";
import { For, createEffect, createSignal, on } from "solid-js";

const ChatHolder = (props: { notification: number }) => {
  const [state, { setChatId, setConversation }] = useChat();
  const conversation = state.conversations[state.chatId];
  const [messages, setMessages] = createSignal(conversation.messages);

  let inputRef: any;
  const sendMessage = async () => {
    const messageText = inputRef.value;
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
    inputRef.value = "";
  };

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
      </div>
      <div class="p-2 flex flex-col overflow-auto gap-1.5">
        <For each={messages()}>
          {(m: any) => (
            <MessageBubble
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
