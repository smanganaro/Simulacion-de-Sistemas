import os
from concurrent.futures import ThreadPoolExecutor
import subprocess

files = ['1-dynamic-L-25-N-50.ari','2-dynamic-L-25-N-100.ari','3-dynamic-L-25-N-200.ari','4-dynamic-L-25-N-300.ari'
,'5-dynamic-L-25-N-400.ari','6-dynamic-L-25-N-500.ari','7-dynamic-L-25-N-700.ari','8-dynamic-L-25-N-1000.ari','9-dynamic-L-25-N-3000.ari']
noise = [0.4]
s_time = 2000

with ThreadPoolExecutor(max_workers=5) as executor:
    for x in noise:
        values = [x]
        for y in files:
            command = 'java -jar ../target/off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar -df ../dynamic/{file} -rc 1 -t {time} -n {noise} -op false'.format(
                    file=y,
                    noise=x,
                    time=s_time,
                )
            print(command)
            executor.submit(subprocess.call, command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            
print("All tasks complete")