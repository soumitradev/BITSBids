import {A} from "@solidjs/router";
import {createSignal} from "solid-js";

export default function YourProductCard({
                                            productId,
                                            description,
                                            imgUrl,
                                            name,
                                            currentBid,
                                            base_price,
                                            closedAt,
                                        }: {
    productId: number;
    description: string;
    imgUrl: string;
    name: string;
    currentBid: number;
    base_price: number;
    closedAt: Date;
}) {
    let [currentDiff, setCurrentDiff] = createSignal(closedAt.getTime() - Date.now());
    let [outputDiff, setOutputDiff] = createSignal("");

    const handleDiff = () => {
        setCurrentDiff(currentDiff() - 1000);
        let output_diff: string = "";
        const diff = Math.floor(currentDiff() / 1000);
        const hours = Math.floor(diff / (60 * 60));
        const minutes = Math.floor((diff % (60 * 60)) / 60);
        const seconds = Math.floor(diff % 60);

        if (hours > 0 || minutes > 0 || seconds > 0) {
            output_diff = `${hours > 0 ? hours + "h" : ""} ${
                minutes > 0 ? minutes + "m" : ""
            } ${seconds > 0 ? seconds + "s" : ""}`;
        } else {
            output_diff = "Auction Closed";
        }
        setOutputDiff(output_diff);
    };

    setInterval(handleDiff, 1000);
    // if you want bg colour bg-[#0F1629]
    return (
        <div class="flex w-[70%] h-48  rounded-lg bg-[#0F1629]  border-2 gap-1 align-center justify-between">
            <div class="flex gap-2 self-stretch">
                <div class=" flex justify-start items-center m-0 min-w-[35%]">
                    <img class="h-full w-full rounded-l-lg object-cover" src={imgUrl} alt="smtg"/>
                </div>
                <div class="flex flex-col justify-center items-start p-2 self-stretch">
                    <div class=" font-bold text-4xl m-1"><h2>{name}</h2></div>
                    <div class=" text-lg text-[#7F8EA3]">{description}</div>
                </div>
            </div>

            <div class="flex p-1 gap-3">
                <div class="flex flex-col justify-center items-center gap-1 p-1">
                    <div class="font-bold text-right text-4xl w-11/12 m-1">&#8377{currentBid}</div>
                    <div class=" text-right text-lg text-[#7F8EA3] w-11/12 whitespace-nowrap">Time
                        Left: {outputDiff()}</div>
                    <div class=" text-right text-lg text-[#7F8EA3] w-11/12">Base Price: &#8377{base_price}
                    </div>
                </div>

                <div
                    class="flex flex-col w-1/4 justify-center items-center self-stretch border-l border-white border-opacity-30">
                    <A class="m-auto p-1" href={`/product/${productId}`}>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" id="IconChangeColor" height="16"
                             width="16" class="opacity-60">
                            <g>
                                <path fill="none" d="M0 0h24v24H0z" id="mainIconPathAttribute" stroke="#FFFFFF"
                                      stroke-width="0"></path>
                                <path
                                    d="M10 3v2H5v14h14v-5h2v6a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h6zm7.707 4.707L12 13.414 10.586 12l5.707-5.707L13 3h8v8l-3.293-3.293z"
                                    id="mainIconPathAttribute" stroke="#DDDDDD"></path>
                            </g>
                        </svg>
                    </A>
                    <hr class="w-full border-white border-opacity-30"/>
                    <A class="m-auto p-1" href={`/product/${productId}/delete`}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-trash opacity-80" viewBox="0 0 16 16" id="IconChangeColor">
                            <path
                                d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"
                                id="mainIconPathAttribute" fill="#ce4040"></path>
                            <path fill-rule="evenodd"
                                  d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"
                                  id="mainIconPathAttribute" fill="#ce4040"></path>
                        </svg>
                    </A>
                </div>
            </div>


        </div>
    )
}