import React, { useEffect, useRef, useState } from 'react'
import './GameScreen.css'

const SAMPLE_TEXTS = [
  'The quick brown fox jumps over the lazy dog.',
  'Practice makes perfect. Keep typing to improve your speed.',
  'Typing games are a fun way to build muscle memory and accuracy.',
  'React lets you build interactive user interfaces with components.',
]

const GAME_DURATION = 60 // seconds

export default function GameScreen(): JSX.Element {
  const [running, setRunning] = useState(false)
  const [timeLeft, setTimeLeft] = useState(GAME_DURATION)
  const [currentText, setCurrentText] = useState<string>(() => pickText())
  const [input, setInput] = useState('')
  const [charIndex, setCharIndex] = useState(0)
  const [correctChars, setCorrectChars] = useState(0)
  const [totalTyped, setTotalTyped] = useState(0)
  const [finished, setFinished] = useState(false)

  const timerRef = useRef<number | null>(null)
  const inputRef = useRef<HTMLInputElement | null>(null)

  useEffect(() => {
    if (running) {
      inputRef.current?.focus()
      timerRef.current = window.setInterval(() => {
        setTimeLeft((t) => {
          if (t <= 1) {
            stopGame()
            return 0
          }
          return t - 1
        })
      }, 1000)
    }
    return () => {
      if (timerRef.current) window.clearInterval(timerRef.current)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [running])

  function pickText(): string {
    return SAMPLE_TEXTS[Math.floor(Math.random() * SAMPLE_TEXTS.length)]
  }

  function startGame() {
    setRunning(true)
    setTimeLeft(GAME_DURATION)
    setCurrentText(pickText())
    setInput('')
    setCharIndex(0)
    setCorrectChars(0)
    setTotalTyped(0)
    setFinished(false)
  }

  function stopGame() {
    setRunning(false)
    setFinished(true)
    if (timerRef.current) {
      window.clearInterval(timerRef.current)
      timerRef.current = null
    }
  }

  function resetGame() {
    if (timerRef.current) {
      window.clearInterval(timerRef.current)
      timerRef.current = null
    }
    setRunning(false)
    setTimeLeft(GAME_DURATION)
    setCurrentText(pickText())
    setInput('')
    setCharIndex(0)
    setCorrectChars(0)
    setTotalTyped(0)
    setFinished(false)
  }

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    const value = e.target.value
    const lastChar = value[value.length - 1]
    const expected = currentText[charIndex]

    if (value.length > input.length) {
      setTotalTyped((n) => n + 1)
      if (lastChar === expected) {
        setCorrectChars((n) => n + 1)
      }
      setCharIndex((i) => i + 1)
      if (charIndex + 1 >= currentText.length) {
        const next = pickText()
        setCurrentText(next)
        setInput('')
        setCharIndex(0)
        return
      }
    } else {
      // deletion
      setCharIndex((i) => Math.max(0, i - 1))
    }
    setInput(value)
  }

  const elapsedSeconds = GAME_DURATION - timeLeft
  const wpm = elapsedSeconds > 0 ? Math.round((correctChars / 5) / (elapsedSeconds / 60)) : 0
  const accuracy = totalTyped > 0 ? Math.round((correctChars / totalTyped) * 100) : 100

  return (
    <div className="game-root">
      <header className="game-header">
        <div className="timer">Time: {timeLeft}s</div>
        <div className="stats">
          <div>WPM: {wpm}</div>
          <div>Accuracy: {accuracy}%</div>
        </div>
      </header>

      <main className="game-main">
        <div className="text-box" aria-live="polite" onClick={() => inputRef.current?.focus()}>
          {renderTextWithHighlights(currentText, charIndex)}
        </div>

        <input
          ref={inputRef}
          className="game-input"
          value={input}
          onChange={handleChange}
          disabled={!running}
          aria-label="Typing input"
          autoComplete="off"
          spellCheck={false}
        />

        <div className="controls">
          {!running && !finished && (
            <button onClick={startGame} className="btn primary">Start</button>
          )}
          {running && (
            <button onClick={stopGame} className="btn">Stop</button>
          )}
          <button onClick={resetGame} className="btn">Reset</button>
        </div>

        {finished && (
          <div className="result">
            <h3>Time's up!</h3>
            <p>WPM: {wpm}</p>
            <p>Accuracy: {accuracy}%</p>
            <p>Correct characters: {correctChars}</p>
            <p>Total typed: {totalTyped}</p>
            <button onClick={startGame} className="btn primary">Play again</button>
          </div>
        )}
      </main>
    </div>
  )
}

function renderTextWithHighlights(text: string, index: number) {
  const chars = text.split('')
  return (
    <div className="text-line">
      {chars.map((ch, i) => {
        let cls = 'char'
        if (i < index) cls += ' typed'
        else if (i === index) cls += ' next'
        return (
          <span key={i} className={cls}>
            {ch}
          </span>
        )
      })}
    </div>
  )
}
