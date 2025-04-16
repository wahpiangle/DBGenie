import ReactMarkdown from "react-markdown";
import { Spinner } from "../ui/spinner";
import { ChatMessage } from "@/types/types";
import remarkGfm from "remark-gfm";
import rehypeRaw from "rehype-raw";
import rehypeSanitize from "rehype-sanitize";

export default function MessageDialog(
    { chat }: { chat: ChatMessage }
) {
    console.log(chat.message);
    const match = chat.message.match(/```markdown\s*([\s\S]*?)\s*```/);
    const message = match ? match[1] : chat.message;
    return (
        <div className={`flex gap-2 ${chat.fromServer ? 'flex-row' : 'flex-row-reverse'}`}>
            <div className=
                {`px-4 py-2 rounded-xl max-w-full overflow-auto
                                ${chat.fromServer ? 'bg-gray-200' : 'bg-blue-400'}
                                text-black`
                }
            >
                {chat.loading ?
                    <Spinner /> :
                    <ReactMarkdown
                        className="prose text-black"
                        remarkPlugins={[remarkGfm]}
                        rehypePlugins={[rehypeRaw, rehypeSanitize]}
                    >
                        {message}
                    </ReactMarkdown>
                }
            </div>
        </div>
    )
}
