import MessageBubble from "./MessageBubble";
import { Avatar, AvatarImage, AvatarFallback } from "./ui/avatar";
import { Label } from "./ui/label";
import { Input } from "~/components/ui/input";
import { BiRegularArrowBack } from "solid-icons/bi";
import { BsSend } from "solid-icons/bs";
import { Button } from "./ui/button";

const ChatHolder = () => {
  return (
    <div class="flex flex-col py-2">
      <div class="flex flex-row items-center w-full gap-2 border-b px-2">
        <BiRegularArrowBack />
        <Avatar class="ml-1">
          <AvatarImage src="https://github.com/sek-consuling.png" />
          <AvatarFallback>?</AvatarFallback>
        </Avatar>
        <Label class="text-lg font-semibold">Jaming Set</Label>
      </div>
      <div class="p-2 flex flex-col gap-1.5">
        <MessageBubble
          text="lorem ipsum fedj wjf swjdf sjsd sjsd fsjf"
          timestamp="6:09 pm"
          fromSelf={true}
        />
        <MessageBubble
          text="lorem ipsum fedj wjf swjdf sjsd sjsd fsjf"
          timestamp="6:09 pm"
          fromSelf={false}
        />
        <MessageBubble
          text="lorem ipsum fedj wjf swjdf sjsd sjsd fsjf"
          timestamp="6:09 pm"
          fromSelf={true}
        />
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
