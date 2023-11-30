import { Avatar, AvatarFallback, AvatarImage } from "~/components/ui/avatar";
import { Label } from "./ui/label";
import { Badge } from "./ui/badge";

const ConversationBubble = (props: {
  title: string;
  latestMessage: string;
  unreadCount: number;
}) => {
  return (
    <div class="flex flex-row justify-between items-center gap-2 p-2 border-t bg-slate-900 hover:bg-slate-600">
      <Avatar class="mr-1">
        <AvatarImage src="https://github.com/sek-consulting.png" />
        <AvatarFallback>EK</AvatarFallback>
      </Avatar>
      <div class="flex flex-col justify-self-start flex-grow mb-0.5">
        <Label class="text-lg font-medium">{props.title}</Label>
        <Label class="text-xs text-slate-400">{props.latestMessage}</Label>
      </div>
      {props.unreadCount > 0 && (
        <Badge class="border-slate-400 rounded-full">
          <Label class="font-semibold">{props.unreadCount}</Label>
        </Badge>
      )}
    </div>
  );
};

export default ConversationBubble;
