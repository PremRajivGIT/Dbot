import sys

import scipy
from transformers import AutoProcessor, MusicgenForConditionalGeneration
from pydub import AudioSegment

# Retrieve text input from command-line arguments
text_input = " ".join(sys.argv[1:])

# Set default max_new_tokens
max_new_tokens = 600

# Check if --max_new_tokens argument is provided
if "--max_new_tokens" in sys.argv:
    index = sys.argv.index("--max_new_tokens")
    if len(sys.argv) > index + 1:
        max_new_tokens = int(sys.argv[index + 1])

processor = AutoProcessor.from_pretrained("facebook/musicgen-small")
model = MusicgenForConditionalGeneration.from_pretrained("facebook/musicgen-small")

inputs = processor(
    text=[text_input],
    padding=True,
    return_tensors="pt",
)

audio_values = model.generate(**inputs, max_new_tokens=max_new_tokens)
sampling_rate = model.config.audio_encoder.sampling_rate
scipy.io.wavfile.write("musicgen_out.wav", rate=sampling_rate, data=audio_values[0, 0].numpy())

# Convert WAV to MP3
audio = AudioSegment.from_wav("musicgen_out.wav")
audio.export("musicgen_out.mp3", format="mp3")
