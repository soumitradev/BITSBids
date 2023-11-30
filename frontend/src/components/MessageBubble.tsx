import { Label } from "./ui/label";
import { cn } from "~/lib/utils";

const MessageBubble = (props: {
  text: string;
  timestamp: string;
  fromSelf: boolean;
}) => {
  return (
    <div
      class={cn(
        "w-fit max-w-[55%] flex flex-col items-center py-1 px-2 border-t gap-0.5 rounded-lg",
        props.fromSelf ? "self-end bg-blue-600" : "self-start bg-slate-600"
      )}
    >
      <Label class="self-start leading-tight">{props.text}</Label>
      <Label class="text-xs text-slate-400 self-end">{props.timestamp}</Label>
    </div>
  );
};

export default MessageBubble;
