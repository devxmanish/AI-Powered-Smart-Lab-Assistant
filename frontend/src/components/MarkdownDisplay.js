import React, { useState, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import rehypeRaw from 'rehype-raw';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

const apiEndpoint = "http://localhost:8080/api/ai/generate";

function MarkdownDisplay() {
  const [markdownText, setMarkdownText] = useState('');
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchMarkdown = async () => {
      setIsLoading(true);
      setError(null);

      const options = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "category": "coding",
            "subject": "Data Structures using C",
            "topic": "insertion operation on queues",
            "additionalKeywords": "easy to understand, suitable comments wherever required",
            "explanationLevel": "none"
        })
      };

      try {
        const response = await fetch(apiEndpoint, options);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        if (data.candidates && data.candidates.length > 0) {
          const generatedText = data.candidates[0].content.parts[0].text;
          setMarkdownText(generatedText);
        } else {
          setMarkdownText("No content generated.");
        }
      } catch (err) {
        console.error("Error fetching or processing Markdown:", err);
        setError("Error loading content. Please try again later.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchMarkdown();
  }, []);

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="markdown-container p-4 bg-gray-900 text-white rounded-xl shadow-lg">
      <div className="markdown-body">
        <ReactMarkdown
          children={markdownText}
          remarkPlugins={[remarkGfm]}
          rehypePlugins={[rehypeRaw]}
          components={{
            code({ node, inline, className, children, ...props }) {
              const match = /language-(\w+)/.exec(className || '');
              return !inline && match ? (
                <SyntaxHighlighter
                  style={oneDark}
                  language={match[1]}
                  PreTag="div"
                  {...props}
                >
                  {String(children).replace(/\n$/, '')}
                </SyntaxHighlighter>
              ) : (
                <code className={className} {...props}>
                  {children}
                </code>
              );
            }
          }}
        />
      </div>
    </div>
  );
}

export default MarkdownDisplay;
