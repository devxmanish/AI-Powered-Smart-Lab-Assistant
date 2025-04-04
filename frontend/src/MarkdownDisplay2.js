import React, { useState, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm'; // For GitHub Flavored Markdown (tables, etc.)
import rehypeRaw from 'rehype-raw'; // To allow raw HTML (if needed, but be careful!)

const apiEndpoint = "http://localhost:8080/api/ai/generate";

function MarkdownDisplay() {

  const [markdownText, setMarkdownText] = useState('');
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchMarkdown = async () => {
      setIsLoading(true);
      setError(null); // Clear any previous errors

      const options = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          },
        body: JSON.stringify({
          "category": "coding",
          "subject": "Data Structures and Algorithm",
          "topic": "searching",
          "additionalKeywords": "linear search with C"
      })
      }

      try {
        const response = await fetch(apiEndpoint, options);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json(); // Assuming JSON response
        console.log(data.candidates[0].content.parts[0].text);
        if (data.candidates && data.candidates.length > 0) {
           const generatedText = data.candidates[0].content.parts[0].text; // Get the generated text
           setMarkdownText(generatedText);
        } else {
          setMarkdownText("No content generated."); // Handle cases where no text is available
        }
      } catch (err) {
        console.error("Error fetching or processing Markdown:", err);
        setError("Error loading content. Please try again later."); // User-friendly error message
      } finally {
        setIsLoading(false);
      }
    };

    fetchMarkdown();
  }, [apiEndpoint]); // Fetch when the apiEndpoint changes

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="markdown-container"> {/* Add a class for styling */}
      <h2>Generated Response</h2>
      <ReactMarkdown
        children={markdownText}
        remarkPlugins={[remarkGfm]} // Use remarkGfm for better Markdown support
        rehypePlugins={[rehypeRaw]} // Only if you absolutely need raw HTML and sanitize carefully!
      />
    </div>
  );
}

export default MarkdownDisplay;