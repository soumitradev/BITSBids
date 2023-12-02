import { Show } from "solid-js";
import ChatHolder from "./ChatHolder";
import ConversationHolder from "./ConversationHolder";
import { useChat } from "~/context/chat";

const ChatWindow = (props: { notification: number }) => {
  const [state] = useChat();

  return (
    <Show
      when={state.chatId !== -1}
      fallback={<ConversationHolder notification={props.notification} />}
    >
      <ChatHolder notification={props.notification} />
    </Show>
  );
};

export default ChatWindow;
