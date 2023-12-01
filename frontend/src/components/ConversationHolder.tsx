import { For, createEffect, createSignal, on, onMount } from "solid-js";
import ConversationBubble from "./ConversationBubble";
import { Card } from "./ui/card";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "./ui/tabs";
import { useChat } from "~/context/chat";

const ConversationHolder = (props: { notification: number }) => {
  const [state, { setConversation, setUserId }] = useChat();

  const [productConversations, setProductConversations] = createSignal([]);
  const [buyerConversations, setBuyerConversations] = createSignal([]);

  onMount(async () => {
    const user = await fetch("/api/user/me");
    const { id } = (await user.json()).data;
    setUserId(id);
    const conversations = await fetch("/api/user/conversations");
    if (conversations.status == 200) {
      const { data } = await conversations.json();
      for (const c of data) {
        const req = await fetch(`/api/product/${c.productId}`);
        const { data: productData } = await req.json();
        console.log(productData);
        c.product = productData.product;
        // console.log(c);
        const req2 = await fetch(`/api/product/${c.productId}/messages`);
        const { data: messageData } = await req2.json();
        console.log(messageData);
        c.messages = messageData;
        console.log(c);
        setConversation(c);
      }
      console.log(state);
      setBuyerConversations(data.filter((c: any) => c.sellerId === id));
      setProductConversations(data.filter((c: any) => c.buyerId === id));
    } else {
      console.error(conversations);
    }
  });

  createEffect(
    on(
      () => props.notification,
      () => {
        console.log("fin: " + props.notification);
        const conversation = state.conversations[props.notification];
        console.log(state.conversations, conversation);
        if (conversation !== undefined) {
          fetch(`/api/product/${conversation.product.id}/messages`).then(
            (r) => {
              r.json().then(({ data }) => {
                console.log(data);
                setConversation({
                  ...conversation,
                  messages: data,
                });
                setProductConversations(
                  //@ts-ignore
                  productConversations().map((c: any) =>
                    c.id === conversation.id ? { ...c, messages: data } : c
                  )
                );
                setBuyerConversations(
                  //@ts-ignore
                  buyerConversations().map((c: any) =>
                    c.id === conversation.id ? { ...c, messages: data } : c
                  )
                );
              });
            }
          );
        }
      }
    )
  );

  return (
    <Tabs defaultValue="products">
      <TabsList class="grid w-full grid-cols-2 rounded-b-none bg-slate-800 p-0">
        <TabsTrigger
          value="products"
          class="h-full border focus-visible:ring-0"
        >
          Products
        </TabsTrigger>
        <TabsTrigger value="buyers" class="h-full border">
          Buyers
        </TabsTrigger>
      </TabsList>
      <TabsContent value="products" class="m-0">
        <Card class="border-none flex flex-col rounded-md overflow-auto max-h-72">
          <For each={productConversations()}>
            {(conversation: any) => (
              <ConversationBubble
                id={conversation.id}
                title={conversation.product.name}
                latestMessage={
                  (conversation.messages[conversation.messages.length - 1]
                    .fromBuyer
                    ? "You: "
                    : "") +
                  conversation.messages[conversation.messages.length - 1].text
                }
                unreadCount={
                  conversation.messages.length -
                  conversation.messages
                    .map((msg: any) => msg.id)
                    .indexOf(conversation.lastReadByBuyerId) -
                  1
                }
              />
            )}
          </For>
        </Card>
      </TabsContent>
      <TabsContent value="buyers" class="m-0">
        <Card class="border-none flex flex-col rounded-md overflow-auto max-h-72">
          <For each={buyerConversations()}>
            {(conversation: any) => (
              <ConversationBubble
                id={conversation.id}
                title={conversation.product.name}
                latestMessage={
                  (conversation.messages[conversation.messages.length - 1]
                    .fromBuyer
                    ? ""
                    : "You: ") +
                  conversation.messages[conversation.messages.length - 1].text
                }
                unreadCount={
                  conversation.messages.length -
                  conversation.messages
                    .map((msg: any) => msg.id)
                    .indexOf(conversation.lastReadBySellerId) -
                  1
                }
              />
            )}
          </For>
        </Card>
      </TabsContent>
    </Tabs>
  );
};

export default ConversationHolder;
