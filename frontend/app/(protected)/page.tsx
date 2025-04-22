'use client';

import ChatPageHeader from "@/components/chat/chat-page-header";
import withAuth from "@/components/hoc/withAuth";
import ChatPageBody from "@/components/chat/chat-page-body";

const ChatPage = () => {
    return (
        <div className="flex flex-col dark:bg-darkSecondary h-full">
            <ChatPageHeader />
            <ChatPageBody />
        </div >
    )
}
export default withAuth(ChatPage);