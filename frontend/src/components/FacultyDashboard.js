"use client"

import { useState, useEffect } from "react"
import { motion, AnimatePresence } from "framer-motion"
import { Panel, PanelGroup, PanelResizeHandle } from "react-resizable-panels"
import { Trash2, X, Moon, Sun, Loader2 } from "lucide-react"
import Button from "./ui/Button"
import Card, { CardContent, CardHeader, CardTitle } from "./ui/Card"
import Textarea from "./ui/Textarea"
import Dialog, { DialogContent, DialogHeader, DialogTitle } from "./ui/Dialog"
// import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "./ui/tooltip"
// import { Switch } from "./ui/switch"
import "./styles.css"

export default function FacultyDashboard() {
  const [code, setCode] = useState("")
  const [explanation, setExplanation] = useState("")
  const [bucket, setBucket] = useState([])
  const [isPreviewOpen, setPreviewOpen] = useState(false)
  const [isMobile, setIsMobile] = useState(false)
  const [isDarkMode, setIsDarkMode] = useState(false)
  const [isGenerating, setIsGenerating] = useState(false)

  useEffect(() => {
    const checkMobile = () => setIsMobile(window.innerWidth < 768)
    checkMobile()
    window.addEventListener("resize", checkMobile)
    return () => window.removeEventListener("resize", checkMobile)
  }, [])

  useEffect(() => {
    document.body.classList.toggle("dark", isDarkMode)
  }, [isDarkMode])

  const generateCode = async () => {
    setIsGenerating(true)
    // Simulate AI generation with a delay
    await new Promise((resolve) => setTimeout(resolve, 1500))
    setExplanation("// Example generated code:\nfunction example() {\n  return 'AI-generated code';\n}")
    setCode("This is a mock explanation of the generated code. The AI creates sample code based on user input.")
    setIsGenerating(false)
  }

  const addToBucket = () => {
    if (code && explanation) {
      setBucket([...bucket, { code, explanation }])
      setCode("")
      setExplanation("")
    }
  }

  const removeFromBucket = (index) => {
    setBucket(bucket.filter((_, i) => i !== index))
  }

  const exportPDF = () => {
    console.log("Exporting PDF:", bucket)
  }

  const getTimeOfDay = () => {
    const hour = new Date().getHours()
    if (hour < 12) return "Morning"
    if (hour < 18) return "Afternoon"
    return "Evening"
  }

  return (
    <div className={`flex flex-col gap-4 p-4 h-screen ${isDarkMode ? "dark" : ""}`}>
      {/* Top Navbar */}
      <motion.div
        initial={{ y: -20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ duration: 0.5 }}
        className="flex justify-between items-center p-4 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-lg shadow-lg"
      >
        <h1 className="text-xl font-bold">👋 Good {getTimeOfDay()}, Faculty!</h1>
        <div className="flex items-center gap-3">
          <img
            src="/placeholder.svg?height=40&width=40"
            className="w-10 h-10 rounded-full border-2 border-white"
            alt="Profile"
          />
          <span className="font-medium">Dr. John Doe</span>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Switch checked={isDarkMode} onCheckedChange={setIsDarkMode} className="ml-4" />
              </TooltipTrigger>
              <TooltipContent>
                <p>Toggle {isDarkMode ? "Light" : "Dark"} Mode</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
          {isDarkMode ? <Moon className="w-5 h-5" /> : <Sun className="w-5 h-5" />}
        </div>
      </motion.div>

      {/* Resizable Main Content */}
      <PanelGroup direction={isMobile ? "vertical" : "horizontal"} className="flex-grow gap-4">
        <Panel defaultSize={50} minSize={30}>
          <Card className="h-full flex flex-col transition-shadow hover:shadow-lg">
            <CardHeader>
              <CardTitle>Code Generator</CardTitle>
            </CardHeader>
            <CardContent className="flex-1 flex flex-col gap-4">
              <Textarea
                value={code}
                onChange={(e) => setCode(e.target.value)}
                placeholder="Enter your coding query..."
                className="flex-1 resize-none transition-all focus:ring-2 focus:ring-blue-400"
              />
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <Button
                      onClick={generateCode}
                      className="w-full bg-blue-600 hover:bg-blue-700 transition-colors"
                      disabled={isGenerating}
                    >
                      {isGenerating ? (
                        <>
                          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                          Generating...
                        </>
                      ) : (
                        "Generate Code"
                      )}
                    </Button>
                  </TooltipTrigger>
                  <TooltipContent>
                    <p>Generate AI code based on your query</p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
            </CardContent>
          </Card>
        </Panel>

        <PanelResizeHandle className="w-2 bg-gray-200 hover:bg-blue-300 transition-colors relative">
          <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-1 h-8 bg-gray-400 rounded-full" />
        </PanelResizeHandle>

        <Panel defaultSize={50} minSize={30}>
          <Card className="h-full flex flex-col transition-shadow hover:shadow-lg">
            <CardHeader>
              <CardTitle>Explanation</CardTitle>
            </CardHeader>
            <CardContent className="flex-1 flex flex-col gap-4">
              <Textarea
                value={explanation}
                readOnly
                className="flex-1 resize-none bg-gray-50 dark:bg-gray-800 transition-all"
                placeholder="AI explanation will appear here..."
              />
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <Button
                      onClick={addToBucket}
                      disabled={!code || !explanation}
                      className="w-full bg-green-600 hover:bg-green-700 transition-colors"
                    >
                      Add to Bucket
                    </Button>
                  </TooltipTrigger>
                  <TooltipContent>
                    <p>Save this code and explanation to your bucket</p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
            </CardContent>
          </Card>
        </Panel>
      </PanelGroup>

      {/* Action Buttons */}
      <div className="flex gap-4">
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                onClick={() => setPreviewOpen(true)}
                className="flex-1 bg-purple-600 hover:bg-purple-700 transition-colors"
              >
                Preview Bucket ({bucket.length})
              </Button>
            </TooltipTrigger>
            <TooltipContent>
              <p>View your saved code snippets and explanations</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                onClick={exportPDF}
                disabled={bucket.length === 0}
                className="flex-1 bg-red-600 hover:bg-red-700 transition-colors"
              >
                Export PDF
              </Button>
            </TooltipTrigger>
            <TooltipContent>
              <p>Export your bucket contents as a PDF</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      </div>

      {/* Preview Modal */}
      <Dialog open={isPreviewOpen} onOpenChange={setPreviewOpen}>
        <DialogContent className="max-h-[80vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="flex justify-between items-center">
              <span>📂 Saved Content ({bucket.length})</span>
              <X
                className="cursor-pointer hover:text-gray-600 transition-colors"
                onClick={() => setPreviewOpen(false)}
              />
            </DialogTitle>
          </DialogHeader>
          <div className="space-y-4">
            <AnimatePresence>
              {bucket.map((item, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -20 }}
                  transition={{ duration: 0.3 }}
                >
                  <Card className="group relative p-4 transition-all hover:shadow-md">
                    <Trash2
                      className="absolute top-2 right-2 w-5 h-5 text-red-500 opacity-0 
                      cursor-pointer hover:text-red-600 transition-opacity group-hover:opacity-100"
                      onClick={() => removeFromBucket(index)}
                    />
                    <p className="font-bold mb-2">📝 Code:</p>
                    <pre className="bg-gray-100 dark:bg-gray-800 p-3 rounded mb-4 overflow-x-auto">
                      <code>{item.code}</code>
                    </pre>
                    <p className="font-bold mb-2">📖 Explanation:</p>
                    <p className="whitespace-pre-wrap">{item.explanation}</p>
                  </Card>
                </motion.div>
              ))}
            </AnimatePresence>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}

