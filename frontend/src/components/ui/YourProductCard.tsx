import {A} from "@solidjs/router";

export default function YourProductCard(props: {
    productId: number;
    description: string;
    imgUrl: string;
    name: string;
    currentBid: number;
    base_price: number;
    bidTime: Date
}) {
    // if you want bg colour bg-[#0F1629]
    return (
        <div
            class="flex w-3/5 h-48  rounded-lg rounded-xl bg-[#0F1629] justify-around border-2 gap-1 align-center">
            <div class=" flex order-2 flex-col justify-center items-start basis-1/2 m-2 p-2">
                <div class=" font-bold text-5xl m-1"><h2>{props.name}</h2></div>
                <div class="m-1 text-xl text-[#7F8EA3]">{props.description}</div>
            </div>

            <div class="flex flex-col order-3 justify-center items-center gap-1 basis-1/5 m-1 pb-3">
                <div
                    class="font-bold text-right text-5xl w-11/12">B{props.currentBid}</div>
                <div class=" text-right text-xl text-[#7F8EA3] w-11/12 whitespace-nowrap">1/1/1970 6:40:56 AM</div>
                <div class=" text-right text-xl text-[#7F8EA3] w-11/12">Base Price: {props.base_price}
                </div>
            </div>
            <div class="basis-1/5 flex justify-center order-1 items-center m-1">
                <img class="p-0 h-3/5 w-4/5 rounded object-fill" src={props.imgUrl} alt="smtg"/>
            </div>
            <div class="flex flex-col order-4 justify-center items-center">
                <A class="my-4 " href={`/product/${props.productId}`}>H</A>
                <A class="my-4 " href={`/product/${props.productId}/delete`}>I</A>


            </div>


        </div>
    )
}