import {A} from "@solidjs/router";

export default function CategoryCard(props: { id: number; imgUrl: string; name: string }) {

    return (
        <A class="flex flex-col w-56 h-60 rounded-lg border-2 gap-1 align-middle" href={`/categoriee/${props.id}`}>
            <img class="p-0 m-0 h-4/5 rounded-lg object-fill" src={props.imgUrl} alt="Category_image"/>
            <p class="font-bold text-xl self-center">{props.name}</p>
        </A>
    )
}