import os
import google.generativeai as genai
from dotenv import load_dotenv
from pylangacq import read_chat

load_dotenv()
api_key = os.getenv("GEMINI_API_KEY")

def extract_transcript_from_cha(cha_path):
    data = read_chat(cha_path)
    child_utterances = data.utterances(participants = "CHI")
    lines = []
    for utt in child_utterances:
        utterance_text = utt.tiers.get("CHI", "")
        lines.append(utterance_text)
    return "\n".join(lines)

def analyze_with_gemini(text, api_key):
    genai.configure(api_key = api_key)
    model = genai.GenerativeModel("gemini-2.0-flash")
    prompt = (
        "You are a medical assistant. The following is a transcription of a child speaking. "
        "Please evaluate the language used, clarity, coherence, and structure. "
        "Identify any speech traits that may indicate characteristics associated with virtual autism. "
        "If the speech sounds typical, mention that clearly and report everything within 200 words.\n\n"
        "Here is the transcript:\n\n"
        + text +
        "\n\nGive a short, clinical evaluation with short reasoning based on the quotes above in simple langauge for the child's parent."
    )
    response = model.generate_content(prompt)
    return response.text.strip()

def evaluate_cha_for_autism(cha_path, api_key):
    transcript = extract_transcript_from_cha(cha_path)
    print("[Transcript]:\n", transcript, "\n")
    analysis = analyze_with_gemini(transcript, api_key)
    print("[LLM Analysis]:\n", analysis, "\n")

if __name__ == "__main__":
    evaluate_cha_for_autism("./EigstiDataset/ASD/1010.cha", api_key)