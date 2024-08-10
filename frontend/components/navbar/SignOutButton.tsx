'use client'

import { DropdownMenuItem } from "@/components/ui/dropdown-menu";
import { LogOut } from "lucide-react";
import { useRouter } from "next/navigation";

const SignOutButton = () => {
  const router = useRouter();
  return (
    <DropdownMenuItem

      onClick={
        async () => {
          // await signOut(idToken);
          router.push("/login");
        }
      }>
      <LogOut className="mr-2 h-4 w-4" />
      <span>Logout</span>
    </DropdownMenuItem>
  )
}

export default SignOutButton