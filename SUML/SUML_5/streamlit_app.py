import streamlit as st
import pandas as pd
import time
import matplotlib as plt
import os
from PIL import Image
import torch
from transformers import pipeline
    
def load_pipeline():
    with st.spinner("Ładuję model "):
        return pipeline(
            "translation",
            model="Helsinki-NLP/opus-mt-en-de"
        )
col1, col2 = st.columns([2, 1])
with col1:
    st.title('Tłumacz z języka angielskiego na język niemiecki')
with col2:
    try:
        image = Image.open('./images.jfif')
        st.image(image)
    except FileNotFoundError:
        st.warning("Nie znaleziono obrazka ./images.jfif")
st.header(
        "Wpisz tekst w języku angielskim w polu poniżej, kliknij w przycisk Tłumacz i poczekaj na wynik tłumaczenia na język niemiecki"
    )
text = st.text_area(label="Wpisz tekst do przetłumaczenia z angielskiego na niemiecki:", placeholder="np. Good Morning")
        
if st.button("Tłumacz"):
    with st.spinner("Tłumaczę tekst, proszę czekać"):
        if text.strip() == "":
            st.warning("Wpisz tekst!!")
        else:
            pipeline = load_pipeline()  
            st.subheader("Przetłumaczony tekst: ")
            answer = pipeline(text)
            st.write(answer[0]["translation_text"])
            st.success("Gotowe!")
st.write("s27568")

        


# st.subheader('Zadanie do wykonania')
# st.write('Wykorzystaj Huggin Face do stworzenia swojej własnej aplikacji tłumaczącej tekst z języka angielskiego na język niemiecki. Zmodyfikuj powyższy kod dodając do niego kolejną opcję, tj. tłumaczenie tekstu. Informacje potrzebne do zmodyfikowania kodu znajdziesz na stronie Huggin Face - https://huggingface.co/docs/transformers/index')
# st.write('🐞 Dodaj właściwy tytuł do swojej aplikacji, może jakieś grafiki?')
# st.write('🐞 Dodaj krótką instrukcję i napisz do czego służy aplikacja')
# st.write('🐞 Wpłyń na user experience, dodaj informacje o ładowaniu, sukcesie, błędzie, itd.')
# st.write('🐞 Na końcu umieść swój numer indeksu')
# st.write('🐞 Stwórz nowe repozytorium na GitHub, dodaj do niego swoją aplikację, plik z wymaganiami (requirements.txt)')
# st.write('🐞 Udostępnij stworzoną przez siebie aplikację (https://share.streamlit.io) a link prześlij do prowadzącego')
