import { Show } from "solid-js";
import ChatHolder from "./ChatHolder";
import ConversationHolder from "./ConversationHolder";
import { useChat } from "~/context/chat";

const ChatWindow = () => {
  const [state, { setChatId }] = useChat();
  return (
    <Show when={state.chatId !== -1} fallback={<ConversationHolder />}>
      <ChatHolder />
    </Show>
  );
};

export default ChatWindow;
