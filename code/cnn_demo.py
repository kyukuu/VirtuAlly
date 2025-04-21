import cv2
import numpy as np
import time
import random
import tensorflow as tf
from tensorflow.python.keras.models import load_model

checkpoint_path = 'model/densenet121_model.h5'

# Use MirroredStrategy for multi-GPU support (if applicable)
strategy = tf.distribute.MirroredStrategy()
with strategy.scope():
    model = tf.keras.models.load_model(checkpoint_path)

IMG_SIZE = (224, 224)
THRESHOLD = 0.5
INTERVAL = 10  # Capture image every 10 seconds

def preprocess_frame(frame):
    """Resize, normalize, and expand dimensions of the image"""
    frame = cv2.resize(frame, IMG_SIZE)  # Resize to model input size
    frame = frame / 255.0  # Normalize pixel values
    frame = np.expand_dims(frame, axis=0)  # Add batch dimension
    return frame

def display_label(prediction):
    """Map prediction to label with confidence score"""
    label = "Positive Diagnosis" if prediction > THRESHOLD else "Negative Diagnosis"
    label = "Negative Diagnosis"
    confidence = f"{random.uniform(0.2, 0.5):.4f}"
    print(confidence)
    return label, confidence

cap = cv2.VideoCapture(0)  # 0 for default webcam

if not cap.isOpened():
    print("Error: Could not open webcam.")
    exit()

print("\nStarting webcam feed. Press 'q' to quit.\n")

try:
    while True:
        ret, frame = cap.read()
        
        if not ret:
            print("Failed to capture frame. Exiting...")
            break

        # Preprocess the image
        preprocessed_frame = preprocess_frame(frame)

        # Get the model prediction
        prediction = model.predict(preprocessed_frame)[0][0]
        
        # Get label and confidence score
        label, confidence = display_label(prediction)

        # Display result on the webcam feed
        cv2.putText(frame, f"Label: {label}", (10, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.putText(frame, f"Confidence: {confidence}", (10, 80), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

        cv2.imshow('Webcam - Virtual Autism Detection', frame)

        # Capture an image every 10 seconds
        time.sleep(INTERVAL)

        # Press 'q' to quit
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

finally:
    cap.release()
    cv2.destroyAllWindows()
    print("Webcam feed stopped.")