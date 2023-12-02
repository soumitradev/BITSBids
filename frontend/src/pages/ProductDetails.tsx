import { useParams } from "@solidjs/router";
import Navbar from "~/components/Navbar";
import { Label } from "~/components/ui/label";
import { For, createSignal, onMount } from "solid-js";
import { Button } from "~/components/ui/button";
import { Progress } from "~/components/ui/progress";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "~/components/ui/dialog";
import { Input } from "~/components/ui/input";
import { showToast } from "~/components/ui/toast";

const ProductDetails = () => {
  const { productId } = useParams();
  const [data, setData] = createSignal({});
  const [preview, setPreview] = createSignal("");
  let [currentDiff, setCurrentDiff] = createSignal(0);
  let [outputDiff, setOutputDiff] = createSignal("");

  onMount(async () => {
    const res = await fetch(`/api/product/${productId}`);
    console.log(res);
    // const { data: d } = await res.json();
    const d = {
      product: {
        id: 7,
        name: "young chinese boy",
        description: "ilorem ipsum niger kill me please",
        sellerId: 1,
        media: [
          "https://flowbite.s3.amazonaws.com/docs/gallery/square/image-1.jpg",
          "https://flowbite.s3.amazonaws.com/docs/gallery/featured/image.jpg",
          "https://flowbite.s3.amazonaws.com/docs/gallery/featured/image-3.jpg",
          "https://flowbite.s3.amazonaws.com/docs/gallery/square/image-5.jpg",
        ],
        basePrice: 0,
        autoSellPrice: null,
        price: 69,
        sold: true,
        createdAt: "2023-12-01T10:55:22.045626Z",
        closedAt: "2023-12-04T16:53:55.683Z",
        currentBidId: 1,
      },
      Categories: [],
      Bids: [
        {
          id: 1,
          productId: 7,
          bidderId: 2,
          price: 69,
          placedAt: "2023-12-01T11:18:00.587Z",
        },
      ],
    };
    console.log(d);
    setData(d);
    setPreview(d?.product?.media[0]);
    // @ts-ignore
    setCurrentDiff(new Date(d?.product?.closedAt).getTime() - Date.now());
  });

  const handleDiff = () => {
    // @ts-ignore
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

  let myRef: any;
  setInterval(handleDiff, 1000);

  const placeBid = () => {
    fetch(`/api/product/${productId}/bid`, {
      method: "POST",
      body: JSON.stringify({ price: myRef.value }),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    }).then((req) => {
      if (req.status != 200) {
        showToast({
          title: "Error while placing bid",
          description: req.status,
          variant: "default",
        });
      }
    });
  };

  return (
    <>
      <Navbar />
      <div class="flex flex-col px-10 pt-4 w-full gap-1.5">
        {/* @ts-ignore */}
        <Label class="mt-4 text-3xl font-bold">{data()?.product?.name}</Label>
        <Label class="text-lg text-slate-400 font-bold">
          {/* @ts-ignore */}
          {data()?.Categories?.join(", ")}
        </Label>
      </div>
      <div class="flex flex-row mt-2 px-10 w-full gap-8">
        <div class="grid gap-4 max-w-[50%]">
          <div>
            <img class="h-auto max-w-full rounded-lg" src={preview()} alt="" />
          </div>
          <div class="grid grid-cols-5 gap-4">
            {/* @ts-ignore */}
            <For each={data()?.product?.media}>
              {(m) => (
                <div onClick={() => setPreview(m)}>
                  <img class="h-auto max-w-full rounded-lg" src={m} alt="" />
                </div>
              )}
            </For>
          </div>
        </div>
        <div class="flex flex-col gap-4 max-w-[50%] items-center">
          <div class="flex flex-row w-full justify-between items-center">
            <Label class="text-6xl font-bold justify-self-start">
              {/* @ts-ignore */}
              &#8377;{data()?.product?.price}
            </Label>
            <Dialog>
              <DialogTrigger
                as={Button}
                class="mt-1.5 ml-auto font-bold justify-self-end text-black text-xl"
              >
                Place Bid
              </DialogTrigger>
              <DialogContent class="sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>Place Bid</DialogTitle>
                </DialogHeader>
                <div class="flex flex-col gap-4 py-4">
                  <Label class="text-lg ">
                    {/* @ts-ignore */}
                    Current Bid: {data()?.product?.price}
                  </Label>
                  <Label for="name" class="text-lg">
                    Bid Amount:
                  </Label>
                  <Input
                    ref={myRef}
                    id="bidAmount"
                    type="number"
                    value="Bid"
                    class="col-span-3"
                  />
                </div>
                <DialogFooter>
                  <Button type="submit" onClick={() => placeBid()}>
                    Save changes
                  </Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>
          <Progress
            value={
              100 -
              (Math.max(0, currentDiff()) * 100) /
                // @ts-ignore
                (new Date(data()?.product?.closedAt)?.getTime() -
                  // @ts-ignore
                  new Date(data()?.product?.createdAt)?.getTime())
            }
            class="w-full h-2 mt-2"
          />
          <div class="flex flex-row w-full justify-between">
            <Label class="text-lg text-slate-400 font-bold justify-self-start">
              {/* @ts-ignore */}
              {outputDiff()}
            </Label>
            <Label class="text-lg text-slate-400 justify-self-end font-bold">
              {/* @ts-ignore */}
              Base Price: &#8377;{data()?.product?.basePrice}
            </Label>
          </div>
          <Label class="pt-2 text-lg text-slate-400 self-start">
            {/* @ts-ignore */}
            {data()?.product?.description}
            lorem ipsum you're moms fkse fdkseffnsefjf fdnfdd did dwks dkscd s
            akdca vka d aias ak caksd ca ddkca adjjc ad cakdcadkdc adksd f
          </Label>
          {/* @ts-ignore */}
          <Button class="mt-4 font-bold text-black text-xl self-start">
            Chat with Seller
          </Button>
        </div>
      </div>
    </>
  );
};

export default ProductDetails;
