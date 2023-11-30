import { ParentComponent, createContext, useContext } from "solid-js";
import { createStore } from "solid-js/store";

export type ChatContextState = {
  readonly chatId: number;
};

export type ChatContextValue = [
  state: ChatContextState,
  actions: {
    setChatId: (chatId: number) => void;
  }
];

export const ChatContext = createContext<ChatContextValue>([
  { chatId: -1 },
  { setChatId: () => undefined },
]);

export const ChatProvider: ParentComponent<{ chatId?: number }> = (props) => {
  const [state, setState] = createStore({
    chatId: props.chatId ?? -1,
  });

  const setChatId = (chatId: number) => setState("chatId", chatId);

  return (
    <ChatContext.Provider value={[state, { setChatId }]}>
      {props.children}
    </ChatContext.Provider>
  );
};

export const useChat = () => useContext(ChatContext);
