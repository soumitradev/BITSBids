import { Textarea } from "~/components/ui/textarea";
import Navbar from "~/components/Navbar";
import { Input } from "~/components/ui/input";
import { Label } from "~/components/ui/label";
import { Button } from "~/components/ui/button";
import createDropzone from "solid-dzone";
import { MultiSelect } from "@digichanges/solid-multiselect";
import { DateTimePicker } from "date-time-picker-solid";
import { createSignal, onMount } from "solid-js";
import { showToast } from "~/components/ui/toast.tsx";
import { Option } from "@digichanges/solid-multiselect/dist/Option";
import { useNavigate } from "@solidjs/router";

const AddProduct = () => {
  const { getRootProps, getInputProps, setRefs, files } =
    createDropzone<HTMLDivElement>({
      multiple: true,
      maxFiles: 8,
      maxSize: 10000000,
    });
  let inputRef!: HTMLInputElement;
  let rootRef!: HTMLDivElement;
  const [categoryOptions, setCategoryOptions] = createSignal<Option[]>([]);
  const [categories, setCategories] = createSignal<Option[]>([]);
  const [dateTimeValue, setDateTimeValue] = createSignal<Date>();
  const [name, setName] = createSignal<string>();
  const [description, setDescription] = createSignal<string>();
  const [media, setMedia] = createSignal<string[]>();
  const navigate = useNavigate();
  const [base, setBase] = createSignal<number>();
  const [autoSell, setAutoSell] = createSignal<number>();

  const create = async () => {
    setMedia([
      "https://hips.hearstapps.com/hmg-prod/images/dog-puppy-on-garden-royalty-free-image-1586966191.jpg?crop=0.752xw:1.00xh;0.175xw,0&resize=1200:*",
    ]);
    const createData = {
      name: name(),
      description: description(),
      basePrice: base(),
      media: media(),
      category: categories(),
      closedAt: dateTimeValue(),
      autoSell: autoSell() ?? undefined,
    };
    const registered = await fetch("/api/product/create", {
      method: "POST",
      body: JSON.stringify(createData),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    const data = await registered.json();
    if (registered.status === 201) {
      navigate("/products/" + data.data.id);
    } else if (registered.status === 401) {
      showToast({
        title: "Unauthorized",
        description: data.data.error + ": " + data.data.cause,
      });
      navigate("/");
    } else if (registered.status === 400) {
      showToast({
        title: "Bad Request",
        description: data.data.error + ": " + data.data.cause,
      });
    } else {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }
  };

  onMount(async () => {
    const response = await fetch("/api/categories");
    if (response.status === 200) {
      const data = await response.json();
      let categories_options: string[] = [];
      data.data.forEach((item: Category) => {
        categories_options.push(item.name);
      });
      setCategoryOptions(categories_options);
    } else if (response.status === 500) {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }
  });

  setTimeout(() => {
    // Refs must be set inside a timeout to ensure the elements have been mounted
    setRefs(rootRef, inputRef);
  });

  return (
    <>
      <Navbar />
      <div class="flex flex-col items-center px-12 pt-4 w-full gap-7">
        <Label class="pt-4 text-3xl font-semibold">Add Product</Label>
        <div class="grid grid-cols-2 px-12 pt-4 w-full items-stretch gap-10 pb-3">
          <div class="flex flex-col gap-5 items-stretch justify-items-stretch border-white">
            <div class="grid w-full items-center gap-1.5">
              <Label for="name">Title</Label>
              <Input
                type="text"
                id="name"
                placeholder="Name"
                onChange={(e) => setName(e.target.value)}
              />
            </div>
            <div class="grid w-full items-center gap-1.5">
              <Label for="description">Description</Label>
              <Textarea
                id="description"
                placeholder="Description..."
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
          </div>
          <div class="flex flex-col gap-5 items-stretch justify-items-stretch border-white">
            <div class="flex gap-2">
              <div class="grid w-full items-center gap-1.5">
                <Label for="title">Base Price</Label>
                <Input
                  type="number"
                  id="basePrice"
                  placeholder="0"
                  onChange={(e) => setBase(parseInt(e.target.value))}
                />
              </div>
              <div class="grid w-full items-center gap-1.5">
                <Label for="title">Auto Sell Price</Label>
                <Input
                  type="number"
                  id="autoSellPrice"
                  placeholder="0"
                  onChange={(e) => setAutoSell(parseInt(e.target.value))}
                />
              </div>
            </div>

            <div class="grid w-full items-center gap-1.5">
              <Label for="title">Category</Label>
              <MultiSelect
                style={{
                  chips: { color: "white", background: "grey" },
                  multiSelectContainer: { color: "white", background: "black" },
                  optionListContainer: { color: "black" },
                }}
                options={categoryOptions()}
                onSelect={(selectedList) => {
                  setCategories(selectedList);
                }}
                onRemove={(selectedList) => {
                  setCategories(selectedList);
                }}
              />
            </div>
            <div class="grid w-full items-center gap-1.5">
              <Label for="title">Deadline</Label>
              <DateTimePicker
                currentDate={new Date()}
                minDate={new Date()}
                enableDateInputField={true}
                enableTimeView={true}
                calendarResponse={() => {
                  setDateTimeValue(
                    new Date()
                    // new Date(
                    //   `${props.year}-${props.month}-${
                    //     props.date
                    //   }, ${props.time.replace(" ", "")}`
                    // )
                  );
                }}
              />
            </div>
          </div>
        </div>
        <Button class="font-bold md:ml-6 ml-3 py-2 h-fit px-3" onClick={create}>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="3"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-plus md:mr-1"
          >
            <path d="M5 12h14" />
            <path d="M12 5v14" />
          </svg>
        </Button>
        <div
          {...getRootProps()}
          ref={rootRef}
          onDrop={() => console.log(files)}
        >
          <input {...getInputProps()} ref={inputRef} />
        </div>
      </div>
    </>
  );
};

export default AddProduct;
