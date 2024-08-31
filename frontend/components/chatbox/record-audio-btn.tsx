"use client"
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { Button } from "../ui/button"
import { Mic } from "lucide-react"
import { useRef, useState, useMemo, useCallback } from "react"
import { useWavesurfer } from '@wavesurfer/react'
import RecordPlugin from 'wavesurfer.js/dist/plugins/record.esm.js'

export default function RecordAudioButton() {
    const containerRef = useRef(null)
    const [audioUrl, setAudioUrl] = useState('test.mp3')
    const [isRecordings, setIsRecording] = useState(false)

    // @ts-ignore
    const { wavesurfer, isPlaying, currentTime, isRecording } = useWavesurfer({
        container: containerRef,
        height: 100,
        waveColor: '#ddd',
        progressColor: '#ff006c',
        barWidth: 0,
        barRadius: 0,
        url: audioUrl,
        plugins: useMemo(() => [RecordPlugin.create({ scrollingWaveform: true })], []),
    })
    const formatTime = (seconds: number) => [seconds / 60, seconds % 60].map((v) => `0${Math.floor(v)}`.slice(-2)).join(':')

    const onPlayPause = useCallback(() => {
        wavesurfer && wavesurfer.playPause()
    }, [wavesurfer])

    const startRecord = async () => {
        const record = wavesurfer?.getActivePlugins()[0]
        // @ts-ignore
        if (record?.isRecording()) {
            console.log('stop recording')
            // @ts-ignore
            await record.stopRecording()
            setIsRecording(false)
            return
        } else {
            // @ts-ignore
            await record?.startRecording()
            setIsRecording(true)
            console.log('start recording')
        }
    }
    return (
        <>
            <div className="w-[500px]" ref={containerRef} />

            <p>Current time: {formatTime(currentTime)}</p>

            <div style={{ margin: '1em 0', display: 'flex', gap: '1em' }}>

                <button onClick={onPlayPause} style={{ minWidth: '5em' }}>
                    {isPlaying ? 'Pause' : 'Play'}
                </button>

                <button onClick={startRecord} style={{ minWidth: '5em' }}>
                    {isRecordings ? 'Stop record' : 'Start record'}
                </button>
            </div>
            <Tooltip>
                <TooltipTrigger asChild>
                    <Button variant="ghost" size="icon" className={`dark:hover:bg-darkSecondary ${isRecordings ? "hidden" : ""}`} >
                        <Mic className="size-4" />
                        <span className="sr-only">Use Microphone</span>
                    </Button>
                </TooltipTrigger>
                <TooltipContent side="top">Use Microphone</TooltipContent>
            </Tooltip>
        </>

    )
}
