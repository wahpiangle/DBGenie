import Markdown from "react-markdown";
import { Spinner } from "../ui/spinner";
import { ChatMessage } from "@/types/types";

export default function MessageDialog(
    { chat }: { chat: ChatMessage }
) {
    return (
        <div className={`flex gap-2 ${chat.fromServer ? 'flex-row' : 'flex-row-reverse'}`}>
            <div className=
                {`px-4 py-2 rounded-xl max-w-full
                                ${chat.fromServer ? 'bg-gray-200' : 'bg-blue-400'}
                                text-black`
                }
            >
                {chat.loading ?
                    <Spinner /> :
                    <Markdown>
                        {chat.message}
                    </Markdown>
                }
            </div>
        </div>
    )
}
