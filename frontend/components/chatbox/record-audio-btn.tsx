"use client"
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { Button } from "../ui/button"
import { Mic, Trash } from "lucide-react"
import { useRef, useState, useMemo, useCallback, useEffect } from "react"
import { useWavesurfer } from '@wavesurfer/react'
import RecordPlugin from 'wavesurfer.js/dist/plugins/record.esm.js'

export default function RecordAudioButton({ isRecording, setIsRecording }: { isRecording: boolean, setIsRecording: (isRecording: boolean) => void }) {
    const containerRef = useRef(null)
    const [recordingTime, setRecordingTime] = useState(0)

    const { wavesurfer, isPlaying } = useWavesurfer({
        container: containerRef,
        height: 50,
        waveColor: '#ddd',
        progressColor: '#ff006c',
        barWidth: 3,
        barRadius: 3,
        barHeight: 3,
        normalize: false,
        plugins: useMemo(() => [RecordPlugin.create({ scrollingWaveform: true, renderRecordedAudio: true })], []),
    })
    const formatTime = (milliseconds: number) => {
        const seconds = milliseconds / 1000
        const minutes = Math.floor(seconds / 60)
        const remainingSeconds = Math.floor(seconds % 60)
        return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
    }
    const onPlayPause = useCallback(() => {
        wavesurfer && wavesurfer.playPause()
    }, [wavesurfer])

    const startRecord = async () => {
        const record = wavesurfer?.getActivePlugins()[0]
        // @ts-ignore
        if (record?.isRecording() && isRecording) {
            // @ts-ignore
            await record.stopRecording()
            // @ts-ignore
            setIsRecording(false)
            setRecordingTime(0)
            return
        } else {
            // @ts-ignore
            await record?.startRecording()
            setIsRecording(true)
            console.log('start recording')
        }
    }

    useEffect(() => {
        const interval = setInterval(() => {
            if (isRecording) {
                setRecordingTime((prev) => prev + 1000)
            }
        }
            , 1000)
        return () => {
            clearInterval(interval)
        }
    }, [isRecording])
    return (
        <>
            {/* <div className={isRecording ? "" : "hidden"}> */}
            <div className="bg-black flex items-center">
                <Button variant="ghost"
                    size="icon"
                    onClick={() => {
                        startRecord()
                    }}
                >
                    <Trash className="size-4" />
                </Button>
                <div className="w-[200px]" ref={containerRef} />
                <p>Recording time: {formatTime(recordingTime)}</p>
                <div style={{ margin: '1em 0', display: 'flex', gap: '1em' }}>
                    <button onClick={onPlayPause} style={{ minWidth: '5em' }}>
                        {isPlaying ? 'Pause' : 'Play'}
                    </button>
                    <button onClick={startRecord} style={{ minWidth: '5em' }}>
                        {isRecording ? 'Stop record' : 'Start record'}
                    </button>
                </div>
            </div>
            <Tooltip>
                <TooltipTrigger asChild>
                    <Button variant="ghost"
                        size="icon"
                        className={`dark:hover:bg-darkSecondary ${isRecording ? "hidden" : ""}`}
                        onClick={() => { setIsRecording(true); startRecord() }}
                    >
                        <Mic className="size-4" />
                    </Button>
                </TooltipTrigger>
                <TooltipContent side="top">Use Microphone</TooltipContent>
            </Tooltip>
        </>

    )
}
