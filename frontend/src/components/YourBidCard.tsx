import { A } from "@solidjs/router";

export default function YourBidCard(props: {
  productId: number;
  description: string;
  imgUrl: string;
  name: string;
  currentBid: number;
  yourBid: number;
  bidTime: Date;
}) {
  // if you want bg colour bg-[#0F1629]
  return (
    <A
      class="flex w-3/5 h-48 rounded-xl hover:bg-[#0F1629] justify-around border-2 gap-1 align-center"
      href={`/products/${props.productId}`}
    >
      <div class=" flex flex-col justify-center items-start basis-1/2 m-3 p-2">
        <div class=" font-bold text-5xl m-1">
          <h2>{props.name}</h2>
        </div>
        <div class="m-1 text-xl text-[#7F8EA3]">{props.description}</div>
      </div>

      <div class="flex flex-col justify-center items-center gap-1 basis-1/4 m-2 pb-3">
        <div
          class={`font-bold text-right text-6xl ${
            props.yourBid >= props.currentBid
              ? "text-[#007e1c]"
              : "text-[#921111]"
          } m-1 w-11/12`}
        >
          &#8377;{props.yourBid}
        </div>
        <div class=" text-right text-xl text-[#7F8EA3] w-11/12 whitespace-nowrap">
          {props.bidTime.toLocaleString()}
        </div>
        <div class=" text-right text-xl text-[#7F8EA3] w-11/12">
          {props.yourBid >= props.currentBid ? "Outbids: " : "Outbid By: "}
          &#8377;{Math.abs(props.yourBid - props.currentBid)}
        </div>
      </div>
      <div class="basis-1/5 flex justify-center items-center m-1">
        <img
          class="p-0 h-3/5 w-4/5 rounded object-fill"
          src={props.imgUrl}
          alt="smtg"
        />
      </div>
    </A>
  );
}
