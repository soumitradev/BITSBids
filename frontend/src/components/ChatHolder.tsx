import MessageBubble from "./MessageBubble";
import { Label } from "./ui/label";
import { Input } from "~/components/ui/input";
import { BiRegularArrowBack } from "solid-icons/bi";
import { BsSend } from "solid-icons/bs";
import { Button } from "./ui/button";
import { useChat } from "~/context/chat";
import { For, onMount } from "solid-js";

const ChatHolder = () => {
  const [state, { setChatId }] = useChat();
  const conversation = state.conversations[state.chatId];
  return (
    <div class="flex flex-col py-2 h-96">
      <div class="flex flex-row items-center w-full gap-2 border-b px-2 pb-1">
        <BiRegularArrowBack onClick={() => setChatId(-1)} />
        <Label class="text-lg font-semibold">{conversation.product.name}</Label>
      </div>
      <div class="p-2 flex flex-col overflow-auto gap-1.5">
        <For each={conversation.messages}>
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
        <Input placeholder="Message" class="focus-visible:ring-0" />
        <Button type="submit" class="px-3" variant="secondary">
          <BsSend size={20} />
        </Button>
      </div>
    </div>
  );
};

export default ChatHolder;
