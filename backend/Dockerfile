FROM oven/bun:debian
WORKDIR /app

# Copy package configuration first for caching purposes
COPY package.json bun.lockb /app/

# Install dependencies
RUN bun install

# Copy the rest of the application code
COPY . /app

# Run Prisma database setup
RUN bunx prisma generate
RUN bunx prisma db push

# Expose application and Prisma Studio ports
EXPOSE 8080/tcp 5556/tcp

# Start both the Bun app and Prisma Studio
CMD ["sh", "-c", "bun index.ts & bunx prisma studio --port 5556"]
