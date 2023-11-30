import ChatHolder from "./ChatHolder";
import ConversationHolder from "./ConversationHolder";
import { useChat } from "~/context/chat";

const ChatWindow = () => {
  const [state] = useChat();

  return state.chatId === -1 ? <ConversationHolder /> : <ChatHolder />;
};

export default ChatWindow;
