import { Badge } from "./ui/badge";
import { Avatar, AvatarImage, AvatarFallback } from "./ui/avatar";
import { Label } from "./ui/label";

const MessageBubble = () => {
  return (
    <div class="w-fit max-w-[60%] flex flex-col items-center py-1 px-2 border-t gap-0.5 bg-blue-600 rounded-lg">
      <Label class="self-start leading-tight">i very much miss cc someone please kill me please</Label>
      <Label class="text-xs text-slate-400 self-end">6:09 pm</Label>
    </div>
  );
};

export default MessageBubble;
