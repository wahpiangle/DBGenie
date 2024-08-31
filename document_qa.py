from transformers import pipeline

nlp = pipeline(
    "document-question-answering",
    model="impira/layoutlm-document-qa",
    device="cpu"
)

print(nlp(
    "https://templates.invoicehome.com/invoice-template-us-neat-750px.png",
    "When is payment due date?"
))