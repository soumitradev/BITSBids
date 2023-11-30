import ConversationBubble from "./ConversationBubble";
import { Card } from "./ui/card";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "./ui/tabs";

const ConversationHolder = () => {
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
        <Card class="border-none flex flex-col rounded-md">
          <ConversationBubble
            title="Jaming Set"
            latestMessage="very surprising i know"
            unreadCount={2}
          />
          <ConversationBubble
            title="Sword"
            latestMessage="You: i'll pop you ra too much cock you're showing ok"
            unreadCount={0}
          />
          <ConversationBubble
            title="Bottle of Water"
            latestMessage="would you like my bo'oh'o'wa'er"
            unreadCount={3}
          />
        </Card>
      </TabsContent>
      <TabsContent value="buyers" class="m-0">
        <Card class="border-none flex flex-col rounded-md">
          <ConversationBubble
            title="Dosa Vada Pongal"
            latestMessage="very surprising i know"
            unreadCount={2}
          />
          <ConversationBubble
            title="Sword"
            latestMessage="You: i'll pop you ra too much cock you're showing ok"
            unreadCount={0}
          />
          <ConversationBubble
            title="Bottle of Water"
            latestMessage="would you like my bo'oh'o'wa'er"
            unreadCount={3}
          />
        </Card>
      </TabsContent>
    </Tabs>
  );
};

export default ConversationHolder;