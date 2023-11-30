import { For, Show, createResource, createSignal, onMount } from "solid-js";
import { isServer } from "solid-js/web";
import { A } from "@solidjs/router";

import { As } from "@kobalte/core";
import ShoSho from "shosho";
import { TbCommand, TbHash, TbSearch } from "solid-icons/tb";

import { Badge } from "~/components/ui/badge";
import { Button } from "~/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTrigger,
} from "~/components/ui/dialog";
import { Input } from "~/components/ui/input";

const shortcut = new ShoSho();

// async function fetchSearch(value: string) {
//   const url = `https://docs-search-qyz9b.ondigitalocean.app/search?collectionName=solid-ui-docs&q=${value}&queryBy=title,contents`;
//   const res = await fetch(url);
//   const data: SearchRes = await res.json();
//   return data;
// }

export default function SearchBar() {
  const [searchValue, setSearchValue] = createSignal("");
  const [isOpen, setIsOpen] = createSignal(false);
  const [data] = createResource(searchValue, () => {});
  data();

  onMount(() => {
    if (!isServer) {
      shortcut.register("CmdOrCtrl+K", () => {
        document.getElementById("search-trigger")?.click();
        return true;
      });
      shortcut.start();
    }
  });

  return (
    <Dialog onOpenChange={setIsOpen} open={isOpen()}>
      <DialogTrigger asChild class="mr-4">
        <As
          component={Button}
          id="search-trigger"
          variant="outline"
          class="text-muted-foreground relative w-full justify-between space-x-4"
        >
          <div class="flex items-center space-x-2">
            <TbSearch />
            <span>Search...</span>
          </div>

          <Badge variant="secondary" class="flex items-center ">
            <TbCommand size={16} />
            <span>K</span>
          </Badge>
        </As>
      </DialogTrigger>

      <DialogContent class="flex h-[80%] flex-col">
        <DialogHeader class="mx-4">
          <Input
            placeholder="Search..."
            value={searchValue()}
            onkeyup={(v) => setSearchValue(v.currentTarget.value)}
          />
        </DialogHeader>

        <DialogDescription>
          <Show
            when={!data.loading}
            fallback={<p class="text-center">Loading...</p>}
          >
            {/* <For each={data()?.hits}>
              {(item, index) => {
                const id = `search-content-${index()}`;
                const titleId = `search-title-${index()}`;

                setTimeout(() => {
                  if (document.getElementById(id)) {
                    document.getElementById(id)!.innerHTML =
                      item.highlight.contents.snippet;
                  }
                  if (
                    document.getElementById(titleId) &&
                    item.highlight.title?.snippet
                  ) {
                    document.getElementById(titleId)!.innerHTML =
                      item.highlight.title.snippet;
                  }
                }, 1);

                return (
                  <A
                    href={item.document.uri}
                    onclick={() => setIsOpen(false)}
                    class="duration-400 animate-in fade-in-0"
                  >
                    <div class="grid grid-cols-[25px_1fr] items-start rounded p-4 hover:bg-black/30 dark:hover:bg-white/30">
                      <TbHash size={20} />
                      <div class="space-y-1">
                        <p
                          class="text-base font-medium leading-none text-black dark:text-white"
                          id={titleId}
                        >
                          {item.document.title}
                        </p>
                        <p class="text-muted-foreground text-sm" id={id}></p>
                        Th
                      </div>
                    </div>
                  </A>
                );
              }}
            </For> */}
          </Show>
        </DialogDescription>
      </DialogContent>
    </Dialog>
  );
}
