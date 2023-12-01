import { Progress } from "./ui/progress.tsx";
import { createSignal } from "solid-js";
import { A } from "@solidjs/router";

export default function ProductCard({
  id,
  imgUrl,
  name,
  price,
  createdAt,
  closedAt,
}: {
  id: number;
  imgUrl: string;
  name: string;
  price: number;
  createdAt: Date;
  closedAt: Date;
}) {
  let [currentDiff, setCurrentDiff] = createSignal(
    closedAt.getTime() - Date.now()
  );
  let [outputDiff, setOutputDiff] = createSignal("");

  const handleDiff = () => {
    setCurrentDiff(currentDiff() - 1000);
    let output_diff: string = "";
    if (Math.floor(currentDiff() / 3600000) > 0) {
      output_diff = `Ends in ${Math.floor(
        currentDiff() / 3600000
      )}h ${Math.floor((currentDiff() % 3600000) / 60000)}min`;
    } else if (Math.floor((currentDiff() % 3600000) / 60000) > 0) {
      output_diff = `Ends in ${Math.floor(
        (currentDiff() % 3600000) / 60000
      )}min`;
    } else if (Math.floor((currentDiff() % (3600000 * 60000)) / 1000) > 0) {
      output_diff = `Ends in ${Math.floor(
        (currentDiff() % (3600000 * 60000)) / 1000
      )}sec`;
    } else {
      output_diff = "Auction Closed";
    }
    setOutputDiff(output_diff);
  };

  setInterval(handleDiff, 1000);

  return (
    <A
      class="flex flex-col w-64 h-80 rounded-lg border-2 gap-2 align-left"
      href={`/products/${id}`}
    >
      <img
        class="p-0 m-0 h-2/3 rounded-lg object-cover"
        src={imgUrl}
        alt="smtg"
      />
      <div class="p-2">
        <span class="font-bold">{name}</span>
        <Progress
          value={
            currentDiff() < 0
              ? 100
              : 100 -
                (currentDiff() * 100) /
                  (closedAt.getTime() - createdAt.getTime())
          }
          class="my-1 mb-2"
        />
        <div class="flex gap-1 justify-between">
          <span class="font-bold">R{price}</span>
          <span class="text-slate-400">{outputDiff()}</span>
        </div>
      </div>
    </A>
  );
}
