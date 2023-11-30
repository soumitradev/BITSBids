import { Card } from "~/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "~/components/ui/tabs";
import ConversationBubble from "./ConversationBubble";

const ChatWindow = () => {
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
          <ConversationBubble />
          <ConversationBubble />
          <ConversationBubble />
        </Card>
      </TabsContent>
      <TabsContent value="buyers" class="m-0">
        <Card class="border-none flex flex-col rounded-md">stonks</Card>
      </TabsContent>
    </Tabs>
  );
};

export default ChatWindow;
