"use client"
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { Button } from "../ui/button"
import { Mic, Pause, PauseCircle, PauseCircleIcon, Play, SendHorizonalIcon, Trash } from "lucide-react"
import { useRef, useState, useMemo, useCallback, useEffect } from "react"
import { useWavesurfer } from '@wavesurfer/react'
import RecordPlugin from 'wavesurfer.js/dist/plugins/record.esm.js'

export default function RecordAudioButton(
    { isRecording,
        setIsRecording,
        hasRecording,
        setHasRecording
    }:
        {
            isRecording: boolean,
            setIsRecording: (isRecording: boolean) => void,
            hasRecording: boolean,
            setHasRecording: (hasRecording: boolean) => void
        }
) {
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
        await record?.startRecording()
        // @ts-ignore
        setIsRecording(true)
    }

    const stopRecord = (recordingSaved: boolean) => {
        const record = wavesurfer?.getActivePlugins()[0]
        // @ts-ignore
        if (record?.isRecording()) {
            // @ts-ignore
            record.stopRecording()
            // @ts-ignore
            setIsRecording(false)
            setRecordingTime(0)
        }
        recordingSaved ? setHasRecording(true) : setHasRecording(false)
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
        <div className="flex items-center">
            <div className={`${!(isRecording || hasRecording) && "hidden"} flex items-center gap-2`}>
                <Button variant="ghost"
                    size="icon"
                    onClick={() => { stopRecord(false) }}
                >
                    <Trash />
                </Button>
                <p>{formatTime(recordingTime)}</p>
                <div className="flex dark:bg-darkSecondary items-center p-2 rounded-lg">
                    {!isRecording &&
                        <Button variant="ghost" size="icon"
                            onClick={onPlayPause}
                        >
                            {
                                isPlaying ? <Pause /> : <Play />
                            }
                        </Button>
                    }
                    <div className={`w-[180px] mx-1`} ref={containerRef} />
                    {isRecording && <Button variant="ghost" size="icon"
                        onClick={() => { stopRecord(true) }}
                    >
                        <PauseCircle className="text-red-600" />
                    </Button>}
                </div>
            </div>
            {
                isRecording ?
                    (
                        <Button variant="ghost"
                            size="icon"
                            className="dark:hover:bg-darkSecondary"
                        >
                            <SendHorizonalIcon />
                        </Button>
                    ) :
                    (
                        <Tooltip>
                            <TooltipTrigger asChild>
                                <Button variant="ghost"
                                    size="icon"
                                    className={` ${isRecording ? "hidden" : ""}`}
                                    onClick={() => { startRecord() }}
                                >
                                    <Mic />
                                </Button>
                            </TooltipTrigger>
                            <TooltipContent side="top">Use Microphone</TooltipContent>
                        </Tooltip>
                    )
            }
        </div>
    )
}
