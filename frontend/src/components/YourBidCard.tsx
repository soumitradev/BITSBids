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
  return (
    <A
      class="flex overflow-hidden xl:w-3/4 w-full md:h-48 h-36 rounded-xl hover:bg-slate-950 transition-all duration-300 ease-in-out justify-around border-2 gap-1 items-center"
      href={`/products/${props.productId}`}
    >
      <div class="flex flex-col justify-center items-start basis-1/2 m-3 mb-4 p-2 max-w-[50%]">
        <div class="font-bold xl:text-5xl md:text-3xl text-xl m-1 line-clamp-1 text-ellipsis">
          {props.name}
        </div>
        <div class="m-1 xl:text-xl md:text-lg text-md text-slate-500 line-clamp-2 text-ellipsis">
          {props.description}
        </div>
      </div>

      <div class="flex flex-col justify-center items-center gap-1 basis-1/4 m-2 pb-3">
        <div
          class={`font-bold text-right xl:text-6xl md:text-5xl text-4xl ${
            props.yourBid >= props.currentBid
              ? "text-green-700"
              : "text-red-800"
          } m-1 w-11/12`}
        >
          &#8377;{props.yourBid}
        </div>
        <div class=" text-right xl:text-xl md:text-lg text-sm text-slate-500 w-11/12 whitespace-nowrap">
          {props.bidTime.toLocaleString()}
        </div>
        <div class=" text-right xl:text-xl md:text-lg text-sm text-slate-500 w-11/12">
          {props.yourBid >= props.currentBid
            ? "Outbids next by "
            : "Outbid by "}
          &#8377;{Math.abs(props.yourBid - props.currentBid)}
        </div>
      </div>
      <div class="lg:w-72 w-52 h-full flex justify-center items-center ml-2 p-0">
        <img
          class="p-0 h-full w-full rounded object-cover"
          src={props.imgUrl}
          alt="product image"
        />
      </div>
    </A>
  );
}
