import { Progress } from "./ui/progress.tsx";
import { createSignal } from "solid-js";
import { A } from "@solidjs/router";

export default function SearchResultCard({
  id,
  imgUrl,
  description,
  name,
  price,
  createdAt,
  closedAt,
}: {
  id: number;
  imgUrl: string;
  description: string;
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
    const diff = Math.floor(currentDiff() / 1000);
    const hours = Math.floor(diff / (60 * 60));
    const minutes = Math.floor((diff % (60 * 60)) / 60);
    const seconds = Math.floor(diff % 60);

    if (hours > 0 || minutes > 0 || seconds > 0) {
      output_diff = `Ends in ${hours > 0 ? hours + "h" : ""} ${
        minutes > 0 ? minutes + "m" : ""
      } ${seconds > 0 ? seconds + "s" : ""}`;
    } else {
      output_diff = "Auction Closed";
    }
    setOutputDiff(output_diff);
  };

  setInterval(handleDiff, 1000);

  return (
    <A
      class="flex xl:w-3/4 w-full h-64 rounded-lg border-2 gap-1"
      href={`/products/${id}`}
    >
      <img
        class="m-0 w-1/3 rounded-l-lg object-cover"
        src={imgUrl}
        alt="smtg"
      />
      <div class="flex flex-col gap-2 align-left p-4 w-full">
        <span class="font-bold text-3xl">{name}</span>
        <span class="text-gray-300 font-light">
          {description.split(" ").slice(0, 25).join(" ")}
        </span>
        <Progress
          value={
            100 -
            (Math.max(0, currentDiff()) * 100) /
              (closedAt.getTime() - createdAt.getTime())
          }
          class="my-1 mb-2 w-10/12"
        />
        <div class="flex gap-2 items-center">
          <span class="font-bold text-lg ">&#8377{price}</span>
          <span class="text-gray-300 text-md font-light">{outputDiff()}</span>
        </div>
        <A
          class="bg-white text-black border-white border-2 rounded-lg w-24 p-2 text-center"
          href={`/products/${id}`}
        >
          Bid Now
        </A>
      </div>
    </A>
  );
}
