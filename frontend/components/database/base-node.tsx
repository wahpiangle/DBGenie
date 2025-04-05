import React from "react";
import { cn } from "@/lib/utils";

export const BaseNode = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement> & { selected?: boolean }
>(({ className, selected, ...props }, ref) => (
  <div
    ref={ref}
    className={cn(
      "relative rounded-md border border-slate-200 bg-white p-5 text-slate-950 dark:border-slate-800 dark:bg-slate-950 dark:text-slate-50",
      className,
      selected ? "border-slate-500 shadow-lg dark:border-slate-400" : "",
      "hover:ring-1",
    )}
    tabIndex={0}
    {...props}
  ></div>
));
BaseNode.displayName = "BaseNode";
