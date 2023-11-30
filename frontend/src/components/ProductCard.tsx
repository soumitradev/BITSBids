import {Progress} from "./ui/progress.tsx"
import {createSignal} from "solid-js";
import {A} from "@solidjs/router";

export default function ProductCard(props: {
    id: number;
    imgUrl: string;
    name: string;
    price: number;
    createdAt: Date;
    closedAt: Date
}) {
    const CLOSE = props.closedAt;
    const START = props.createdAt;
    const TOTAL_TIME = CLOSE.getTime() - START.getTime();
    let [current_diff, setCurrentDiff] = createSignal(CLOSE.getTime() - Date.now());
    let [outputDiff, setOutputDiff] = createSignal("");
    const handleDiff = () => {
        setCurrentDiff(current_diff() - 1000);
        let output_diff: string = ""
        if (Math.floor(current_diff() / 3600000) > 0) {
            output_diff = `Ends in ${Math.floor(current_diff() / 3600000)}h ${Math.floor(current_diff() % 3600000 / 60000)}min`;
        } else if (Math.floor((current_diff() % 3600000) / 60000) > 0) {
            output_diff = `Ends in ${Math.floor(current_diff() % 3600000 / 60000)}min`;
        } else if (Math.floor((current_diff() % (3600000 * 60000)) / 1000) > 0) {
            output_diff = `Ends in ${Math.floor(current_diff() % (3600000 * 60000) / 1000)}sec`
        } else {
            output_diff = "Auction Closed"
        }
        setOutputDiff(output_diff)
    }
    setInterval(handleDiff, 1000)
    return (
        <A class="flex flex-col w-48 h-72 rounded-lg border-2 gap-1 align-left" href={`/products/${props.id}`}>
            <img class="p-0 m-0 h-2/3 rounded-lg  object-fill" src={props.imgUrl} alt="smtg"/>
            <div class="p-2">
                <h3>{props.name}</h3>
                <Progress value={100 - (current_diff() * 100 / TOTAL_TIME)}/>
                <div class="flex gap-1 pb-3">

                    <p><strong>R{props.price}</strong> {outputDiff()}</p>
                </div>
            </div>
        </A>
    )
}