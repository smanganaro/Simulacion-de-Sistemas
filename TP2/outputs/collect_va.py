import os
from concurrent.futures import ThreadPoolExecutor
import subprocess

files = ['6-dynamic2-L-25-N-500.ari','2-dynamic2-L-25-N-100.ari']
noise = [0.1]
s_time = 2000

with ThreadPoolExecutor(max_workers=5) as executor:
    for x in noise:
        values = [x]
        for y in files:
            command = 'java -jar ../target/off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar \
            -df ../dynamic/{file} -rc 1 -t {time} -n {noise} -op false'.format(
                    file=y,
                    noise=x,
                    time=s_time,
                )
            print(command)
            executor.submit(subprocess.call, command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            
print("All tasks complete")