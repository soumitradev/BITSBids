import "attr-accept";

import {Textarea} from "~/components/ui/textarea";
import Navbar from "~/components/Navbar";
import {Input} from "~/components/ui/input";
import {Label} from "~/components/ui/label";
import createDropzone from "solid-dzone";
import {MultiSelect} from '@digichanges/solid-multiselect';
import {DateTimePicker} from 'date-time-picker-solid'
import {onMount} from "solid-js";
import {showToast} from "~/components/ui/toast.tsx";
import {Option} from "@digichanges/solid-multiselect/dist/Option";


const AddProduct = () => {
  const {getRootProps, getInputProps, setRefs, files, clearFiles} =
      createDropzone<HTMLDivElement>({multiple: true, maxFiles: 8, maxSize: 10000000}); // You MUST specify the type
  // of the
  // root element for
  // proper
  // typing
  let dateTimeValue = new Date();
  let inputRef!: HTMLInputElement;
  let rootRef!: HTMLDivElement;
  let categories_options: string[] = [];
  let categories: Option[] = [];
  onMount(async () => {
    const response = await fetch("/api/categories");
    if (response.status === 200) {
      const data = await response.json();
      data.data.forEach((item: Category) => {
        categories_options.push(item.name)
      });
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
        <Navbar/>
        <div class="flex flex-col items-center px-12 pt-4 w-full gap-7">
          <Label class="pt-4 text-3xl font-semibold">Add Product</Label>
          <div class="grid grid-cols-2 px-12 pt-4 w-full items-stretch gap-10 pb-3">
            <div class="flex flex-col gap-5 items-stretch justify-items-stretch border-white">
              <div class="grid w-full items-center gap-1.5">
                <Label for="title">Title</Label>
                <Input type="text" id="title" placeholder="Title"/>
              </div>
              <div class="grid w-full items-center gap-1.5">
                <Label for="description">Description</Label>
                <Textarea id="description" placeholder="Description..."/>
              </div>
            </div>
            <div class="flex flex-col gap-5 items-stretch justify-items-stretch border-white">
              <div class="flex gap-2">
                <div class="grid w-full items-center gap-1.5">
                  <Label for="title">Base Price</Label>
                  <Input type="number" id="basePrice" placeholder="0"/>
                </div>
                <div class="grid w-full items-center gap-1.5">
                  <Label for="title">Auto Sell Price</Label>
                  <Input type="number" id="autoSellPrice" placeholder="0"/>
                </div>
              </div>


              <div class="grid w-full items-center gap-1.5">
                <Label for="title">Category</Label>
                <MultiSelect
                    style={{
                      chips: {color: "white", "background": "grey"},
                      multiSelectContainer: {color: "white", "background": "black"},
                      optionListContainer: {color: "black"}
                    }}
                    options={categories_options}
                    onSelect={(selectedList) => {
                      categories = selectedList
                    }}
                    onRemove={(selectedList) => {
                      categories = selectedList
                    }}
                />
              </div>
              <div class="grid w-full items-center gap-1.5">
                <Label for="title">Deadline</Label>
                <DateTimePicker currentDate={new Date()} minDate={new Date()} enableDateInputField={true}
                                enableTimeView={true} calendarResponse={(props) => {
                  dateTimeValue = new Date(`${props.year}-${props.month}-${props.date}, ${props.time.replace(" ", "")}`);
                }}/>
              </div>
            </div>
          </div>
          <div {...getRootProps()} ref={rootRef}>
            <input {...getInputProps()} ref={inputRef}/>
          </div>
        </div>
      </>
  );
};

export default AddProduct;
