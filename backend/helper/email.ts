import { Resend } from "resend"

const sendEmail = async (to: string, subject: string, html: string) => {
    const resend = new Resend(process.env.RESEND_API_KEY)
    const { data, error } = await resend.emails.send({
        from: "Acme <onboarding@resend.dev>",
        to,
        subject,
        html
    })
    return { data, error }
}

export { sendEmail }