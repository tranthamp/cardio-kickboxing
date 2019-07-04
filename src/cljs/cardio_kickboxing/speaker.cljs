(ns cardio-kickboxing.speaker)

(defn say
  ([message] (say message nil))
  ([message callback]
    (let [utterance (new js/SpeechSynthesisUtterance)
          voices (js/window.speechSynthesis.getVoices())]
      ;FIXME: Figure out how to change the voice
      ;(set! (.-voice utterance) (nth voices 48))
      (set! (.-lang utterance) "en-US")
      (set! (.-text utterance) message)
      (.addEventListener utterance "end" callback)
      (js/speechSynthesis.speak utterance))))
