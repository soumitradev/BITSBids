import { ParentComponent, createContext, useContext } from "solid-js";
import { createStore } from "solid-js/store";

export type ChatContextState = {
  readonly chatId: number;
  readonly userId: number;
  readonly conversations: {
    [id: number]: {
      id: number;
      productId: number;
      buyerId: number;
      sellerId: number;
      product: {
        id: number;
        name: string;
        sellerId: number;
        price: number;
        description: string;
        images: string[];
      };
      messages: [
        {
          id: number;
          fromBuyer: boolean;
          text: string;
          sentAt: string;
        }
      ];
    };
  };
};

export type ChatContextValue = [
  state: ChatContextState,
  actions: {
    setChatId: (chatId: number) => void;
    setConversation: (conversation: any) => void;
    setUserId: (userId: any) => void;
  }
];

export const ChatContext = createContext<ChatContextValue>([
  { chatId: -1, userId: -1, conversations: {} },
  {
    setChatId: () => undefined,
    setConversation: () => undefined,
    setUserId: () => undefined,
  },
]);

export const ChatProvider: ParentComponent<{ chatId?: number }> = (props) => {
  const [state, setState] = createStore({
    chatId: props.chatId ?? -1,
    userId: -1,
    conversations: {},
  });

  const setChatId = (chatId: number) => setState("chatId", chatId);
  const setConversation = (conversation: any) =>
    setState("conversations", (c: any) => ({
      ...c,
      [conversation.id]: conversation,
    }));
  const setUserId = (userId: number) => setState("userId", userId);

  return (
    <ChatContext.Provider
      value={[state, { setChatId, setConversation, setUserId }]}
    >
      {props.children}
    </ChatContext.Provider>
  );
};

export const useChat = () => useContext(ChatContext);
