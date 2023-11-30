import { Avatar, AvatarFallback, AvatarImage } from "~/components/ui/avatar";
import { Label } from "./ui/label";
import { Badge } from "./ui/badge";

const ConversationBubble = () => {
  return (
    <div class="flex flex-row justify-between items-center gap-2 p-2 border-t bg-slate-900 hover:bg-slate-600">
      <Avatar class="mr-1">
        <AvatarImage src="https://github.com/sek-consulting.png" />
        <AvatarFallback>EK</AvatarFallback>
      </Avatar>
      <div class="flex flex-col justify-self-start flex-grow mb-0.5">
        <Label class="text-lg font-medium">Jaming Set</Label>
        <Label class="text-xs text-slate-400">You: ill pop you ra</Label>
      </div>
      <Badge class="border-slate-400 rounded-full">
        <Label class="font-semibold">2</Label>
      </Badge>
    </div>
  );
};

export default ConversationBubble;
