import { Label } from "./ui/label";
import { cn } from "~/lib/utils";

const MessageBubble = (props: {
  id: string;
  text: string;
  timestamp: string;
  fromSelf: boolean;
}) => {
  return (
    <div
      class={cn(
        "w-fit max-w-[55%] flex flex-col items-center py-1 px-2 border-t gap-0.5 rounded-lg",
        props.fromSelf ? "self-end bg-blue-700" : "self-start bg-slate-600"
      )}
      id={props.id}
    >
      <Label class="self-start leading-tight text-slate-200">
        {props.text}
      </Label>
      <Label class="text-xs text-slate-400 self-end">
        {new Date(props.timestamp).toLocaleString()}
      </Label>
    </div>
  );
};

export default MessageBubble;
