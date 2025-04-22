'use client';

import ChatPageHeader from "@/components/chat/chat-page-header";
import ChatPage from "@/components/chat/ChatPage"
import withAuth from "@/components/hoc/withAuth";

const Dashboard = () => {
    return (
        <div className="flex flex-col dark:bg-darkSecondary h-full">
            <ChatPageHeader />
            <ChatPage />
        </div >
    )
}
export default withAuth(Dashboard);