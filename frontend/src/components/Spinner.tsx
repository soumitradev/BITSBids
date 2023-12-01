const Spinner = () => {
  return (
    <>
      <div
        class={
          "flex h-12 w-12 animate-spin items-center justify-center rounded-[50%] border-8 border-t-8 border-slate-200 border-t-slate-500"
        }
      >
        <div class={"h-8 w-8 rounded-[50%] bg-slate-0"}></div>
      </div>
    </>
  );
};

export default Spinner;
